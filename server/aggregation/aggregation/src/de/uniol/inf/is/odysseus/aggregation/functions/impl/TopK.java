/**
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.aggregation.functions.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

/**
 * @author Cornelius Ludmann
 *
 */
public class TopK<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static Logger LOG = LoggerFactory.getLogger(TopK.class);
	private static final long serialVersionUID = -7975253456214907121L;

	protected Serializable defaultGroupingKey = "__DEFAULT_GROUPING_KEY";

	protected final int k;

	protected final int[] uniqueAttributeIndices;
	protected final Map<Object, T> mapByUniqueAttributes = new HashMap<>();

	protected final T removeLowerEqualsThan;

	protected final SortedSet<T> elements;

	protected final Comparator<T> comparator;
	protected final SDFSchema subSchema;

	private transient final List<Object> lastTopKOutput;

	/**
	 * If set to true, elements with same score are ordered so that the newest are
	 * on top. If set to false, elements with same score are ordered so that the
	 * oldest are on top.
	 */
	protected final boolean onEqualScoreNewestOnTop;
	private int[] scoringAttributes;
	protected final boolean descending;
	protected final boolean alwaysOutput;

	public TopK() {
		super();
		this.elements = null;
		this.comparator = null;
		this.k = 0;
		this.subSchema = null;
		this.lastTopKOutput = null;
		this.onEqualScoreNewestOnTop = true;
		this.removeLowerEqualsThan = null;
		this.uniqueAttributeIndices = null;
		this.descending = true;
		this.alwaysOutput = false;
	}

	@SuppressWarnings("unchecked")
	public TopK(final TopK<M, T> other) {
		super(other);
		this.comparator = other.comparator;
		this.elements = new TreeSet<>(comparator);
		this.k = other.k;
		this.subSchema = other.subSchema.clone();
		this.lastTopKOutput = new ArrayList<>(k);
		this.onEqualScoreNewestOnTop = other.onEqualScoreNewestOnTop;
		this.uniqueAttributeIndices = Arrays.copyOf(other.uniqueAttributeIndices, other.uniqueAttributeIndices.length);
		this.removeLowerEqualsThan = other.removeLowerEqualsThan == null ? null
				: (T) other.removeLowerEqualsThan.clone();
		this.scoringAttributes = Arrays.copyOf(other.scoringAttributes, other.scoringAttributes.length);
		this.descending = other.descending;
		this.alwaysOutput = other.alwaysOutput;
	}

	public TopK(final int k, final int[] attributes, final String outputAttributeName, final SDFSchema subSchema,
			final boolean onEqualScoreNewestOnTop, final T removeLowerEqualsThan, final int[] uniqueAttributeIndices,
			final int[] scoringAttributes, boolean descending, boolean alwaysOutput) {
		super(attributes, true, new String[] { outputAttributeName });
		this.k = k;
		// TODO: This comparator is not consistent with equals as
		// explained by the Comparable class specification. Therefore, the
		// multiset violates the java.util.Collection contract,
		// which is specified in terms of Object.equals.
		// TreeMultiset Javadoc says: "In all cases, this implementation uses
		// Comparable.compareTo or Comparator.compare instead of Object.equals
		// to determine equivalence of instances." -> no problem?
		this.comparator = new TupleComparator<M, T>(scoringAttributes, onEqualScoreNewestOnTop, descending);
		this.elements = new TreeSet<>(comparator);
		this.subSchema = subSchema.clone();
		this.lastTopKOutput = new ArrayList<>(k);
		this.onEqualScoreNewestOnTop = onEqualScoreNewestOnTop;
		this.removeLowerEqualsThan = removeLowerEqualsThan;
		this.uniqueAttributeIndices = Arrays.copyOf(uniqueAttributeIndices, uniqueAttributeIndices.length);
		this.scoringAttributes = Arrays.copyOf(scoringAttributes, scoringAttributes.length);
		this.descending = descending;
		this.alwaysOutput = alwaysOutput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#addNew(de.uniol.inf.is.odysseus.core.
	 * collection.Tuple)
	 */
	@Override
	public void addNew(final T newElement) {
		final boolean add = removeLowerEqualsThan == null || !isLowerOrEquals(newElement, removeLowerEqualsThan);

		if (uniqueAttributeIndices != null) {
			final Object uniqueAttrKey = AggregationPO.getGroupKey(newElement, uniqueAttributeIndices,
					defaultGroupingKey);
			final T e = mapByUniqueAttributes.get(uniqueAttrKey);
			if (e != null) {
				elements.remove(e);
				if (!add) {
					mapByUniqueAttributes.remove(uniqueAttrKey);
				}
			}
			if (add) {
				mapByUniqueAttributes.put(uniqueAttrKey, newElement);
			}
		}

		if (add) {
			elements.add(newElement);
		}
	}

	private boolean isLowerOrEquals(final T newElement, final T removeLowerEqualsThan2) {
		if (descending) {
			return (-comparator.compare(newElement, removeLowerEqualsThan2)) <= 0;
		} else {
			return (comparator.compare(newElement, removeLowerEqualsThan2)) <= 0;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#removeOutdated(java.util.Collection,
	 * de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public void removeOutdated(final Collection<T> outdatedElements, final T trigger, final PointInTime pointInTime) {
		elements.removeAll(outdatedElements);
		if (uniqueAttributeIndices != null) {
			mapByUniqueAttributes.entrySet().removeAll(outdatedElements);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#evalute(de.uniol.inf.is.odysseus.core.
	 * collection.Tuple, de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Object[] evalute(final T trigger, final PointInTime pointInTime) {
		boolean sameAsLastOutput = true;
		int i = 0;
		final List<Object> topKList = new ArrayList<>(k);

		// Use the first k elements from the sorted list of element
		for (final Iterator<T> iter = elements.iterator(); iter.hasNext() && i < k;) {
			Object e;

			if (subSchema.size() == 1) {
				e = getFirstAttribute(iter.next());
			} else {
				e = getAttributesAsTuple(iter.next());
			}

			if (i >= lastTopKOutput.size()) {
				sameAsLastOutput = false;
				lastTopKOutput.add(e);
			} else if (!e.equals(lastTopKOutput.get(i))) {
				sameAsLastOutput = false;
				lastTopKOutput.set(i, e);
			}
			topKList.add(e);

			++i;
		}
		if (i < lastTopKOutput.size()) {
			sameAsLastOutput = false;
			for (int j = lastTopKOutput.size() - 1; j >= i; --j) {
				lastTopKOutput.remove(j);
			}
		}
		if (sameAsLastOutput && !this.alwaysOutput) {
			return new Object[] { null };
		}
		return new Object[] { topKList };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * getOutputAttributes()
	 */
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		if (subSchema.size() == 1) {
			for (final SDFDatatype listType : SDFDatatype.getLists()) {
				if (listType.getSubType().equals(subSchema.get(0).getDatatype())) {
					return Collections
							.singleton(new SDFAttribute(null, outputAttributeNames[0], listType, null, null, null));
				}
			}
			LOG.warn("Could not find list type with subtype '{}'. Use list type without subtype ('{}') instead.",
					subSchema.get(0).getDatatype(), SDFDatatype.LIST);
			return Collections
					.singleton(new SDFAttribute(null, outputAttributeNames[0], SDFDatatype.LIST, null, null, null));
		} else {
			final SDFDatatype datatype = SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST, SDFDatatype.TUPLE,
					subSchema);
			return Collections.singleton(new SDFAttribute(null, outputAttributeNames[0], datatype, null, null, null));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractIncrementalAggregationFunction#clone()
	 */
	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new TopK<>(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.factory.
	 * IAggregationFunctionFactory#checkParameters(java.util.Map,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver)
	 */
	@Override
	public boolean checkParameters(final Map<String, Object> parameters, final IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, "TOP_K", -1);
		return k > 0;
	}

	private final static String TOP_K = "TOP_K";
	private final static String NEWEST_ON_TOP = "NEWEST_ON_TOP";
	private final static String MIN_SCORE = "MIN_SCORE";
	private final static String UNIQUE_ATTR = "UNIQUE_ATTR";
	private final static String SCORING_ATTRIBUTES = "SCORING_ATTRIBUTES";
	private final static String DESCENDING = "DESCENDING";
	// No null-values if same output as before
	private final static String ALWAYS_OUTPUT = "ALWAYS_OUTPUT";

	@Override
	public IAggregationFunction createInstance(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, TOP_K, -1);
		final boolean newestOnTop = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters,
				NEWEST_ON_TOP, true);
		T removeLowerEqualsThan = getMinScore(parameters, attributeResolver);
		final int[] uniqueAttributeIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, UNIQUE_ATTR);
		final int[] scoringAttributes = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
				attributeResolver, SCORING_ATTRIBUTES);
		final boolean descending = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters,
				DESCENDING, true);
		final boolean alwaysOutput = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters,
				ALWAYS_OUTPUT, false);
		String outputAttributeName = getOutputAttributeName(parameters);

		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver, 0, false);

		final SDFSchema outputSchema = getTupleOutputSchema(attributes, attributeResolver);
		return new TopK<>(k, attributes, outputAttributeName, outputSchema, newestOnTop, removeLowerEqualsThan,
				uniqueAttributeIndices, scoringAttributes, descending, alwaysOutput);
	}

	/**
	 * Calculates the output schema for the tuples (could be a subschema or the
	 * whole schema)
	 */
	private SDFSchema getTupleOutputSchema(int[] attributes, IAttributeResolver attributeResolver) {
		final SDFSchema outputSchema;
		if (attributes == null) {
			outputSchema = attributeResolver.getSchema().get(0);
		} else {
			outputSchema = createSubSchemaForOutputAttributes(attributes, attributeResolver);
		}
		return outputSchema;
	}

	/**
	 * Creates a schema which is used as the subschema for the output tuples
	 */
	private SDFSchema createSubSchemaForOutputAttributes(int[] attributes, IAttributeResolver attributeResolver) {
		final List<SDFAttribute> attr = new ArrayList<>();
		for (final int idx : attributes) {
			attr.add(attributeResolver.getSchema().get(0).getAttribute(idx).clone());
		}
		final SDFSchema subSchema = SDFSchemaFactory.createNewTupleSchema("", attr);
		return subSchema;
	}

	/**
	 * Determined the name of the output attribute
	 */
	private String getOutputAttributeName(final Map<String, Object> parameters) {
		String outputAttributeName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		if (outputAttributeName == null) {
			outputAttributeName = "TopK";
		}
		return outputAttributeName;
	}

	/**
	 * @return A stream element which contains the min score to be compared to. Or
	 *         null, if no min score is given.
	 */
	@SuppressWarnings("unchecked")
	private T getMinScore(final Map<String, Object> parameters, final IAttributeResolver attributeResolver) {
		T removeLowerEqualsThan = null;
		final long minScore = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, MIN_SCORE,
				Integer.MIN_VALUE);
		if (minScore != Integer.MIN_VALUE) {
			final Object[] nullObjects = new Object[attributeResolver.getSchema().get(0).size()];
			Arrays.fill(nullObjects, minScore);
			removeLowerEqualsThan = (T) new Tuple<M>(nullObjects, false);
		}
		return removeLowerEqualsThan;
	}

	/**
	 * Comparator used to compare the tuples so be able to sort them.
	 */
	private static class TupleComparator<M extends ITimeInterval, T extends Tuple<M>>
			implements Comparator<T>, Serializable {
		private static final long serialVersionUID = 1L;

		private final int[] attributes;
		private final boolean onEqualScoreNewestOnTop;
		private final boolean descending;

		/**
		 * @param onEqualScoreNewestOnTop
		 *            If set to true, elements with same score are ordered so that the
		 *            newest are on top. If set to false, elements with same score are
		 *            ordered so that the oldest are on top.
		 * @param attributes
		 *            The attributes to use when ordering
		 * @param descending
		 *            If set to true, the result will be descending
		 * 
		 */
		public TupleComparator(final int[] attributes, final boolean onEqualScoreNewestOnTop,
				final boolean descending) {
			this.attributes = Arrays.copyOf(attributes, attributes.length);
			this.onEqualScoreNewestOnTop = onEqualScoreNewestOnTop;
			this.descending = descending;
		}

		@Override
		public int compare(final T o1, final T o2) {
			int result = 0;
			// compare the attributes in order of appearance
			for (int i = 0; i < attributes.length && result == 0; ++i) {
				result = Comparator.naturalOrder().compare(o1.getAttribute(attributes[i]),
						o2.getAttribute(attributes[i]));
			}
			if (o1.getMetadata() != null && o2.getMetadata() != null) {
				// compare the start timestamp
				if (result == 0) {
					result = o1.getMetadata().getStart().compareTo(o2.getMetadata().getStart());
					if (!onEqualScoreNewestOnTop) {
						result = -result;
					}
				}
				if (result == 0) {
					result = Long.compare(o1.getMetadata().getStart().tiBreaker, o2.getMetadata().getStart().tiBreaker);
					if (!onEqualScoreNewestOnTop) {
						result = -result;
					}
				}
			}
			if (descending) {
				return -result;
			} else {
				return result;
			}
		}
	}

}
