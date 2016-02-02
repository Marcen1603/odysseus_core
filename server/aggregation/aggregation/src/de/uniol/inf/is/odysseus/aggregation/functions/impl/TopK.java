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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Cornelius Ludmann
 *
 */
public class TopK<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	protected final int k;
	protected final SortedSet<T> elements;
	protected final Comparator<T> comparator;
	protected final SDFSchema subSchema;
	private final List<T> lastTopKOutput;
	/**
	 * If set to true, elements with same score are ordered so that the newest
	 * are on top. If set to false, elements with same score are ordered so that
	 * the oldest are on top.
	 */
	protected final boolean onEqualScoreNewestOnTop;

	public TopK() {
		super();
		elements = null;
		comparator = null;
		k = 0;
		this.subSchema = null;
		this.lastTopKOutput = null;
		this.onEqualScoreNewestOnTop = true;
	}

	public TopK(final TopK<M, T> other) {
		super(other);
		this.comparator = other.comparator;
		this.elements = new TreeSet<>(comparator);
		this.k = other.k;
		this.subSchema = other.subSchema.clone();
		this.lastTopKOutput = new ArrayList<>(k);
		this.onEqualScoreNewestOnTop = other.onEqualScoreNewestOnTop;
	}

	public TopK(final int k, final int[] attributes, final String outputAttributeName, final SDFSchema subSchema,
			final boolean onEqualScoreNewestOnTop) {
		super(attributes, true, new String[] { outputAttributeName });
		this.k = k;
		// TODO: This comparator is not consistent with equals as
		// explained by the Comparable class specification. Therefore, the
		// multiset violates the java.util.Collection contract,
		// which is specified in terms of Object.equals.
		// TreeMultiset Javadoc says: "In all cases, this implementation uses
		// Comparable.compareTo or Comparator.compare instead of Object.equals
		// to determine equivalence of instances." -> no problem?
		comparator = new Comparator<T>() {

			@Override
			public int compare(final T o1, final T o2) {
				int result = 0;
				// compare the attributes in order of appearance
				for (int i = 0; i < attributes.length && result == 0; ++i) {
					result = Comparator.naturalOrder().compare(o1.getAttribute(attributes[i]),
							o2.getAttribute(attributes[i]));
				}
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
				return -result;
			}
		};
		elements = new TreeSet<>(comparator);
		this.subSchema = subSchema.clone();
		this.lastTopKOutput = new ArrayList<>(k);
		this.onEqualScoreNewestOnTop = onEqualScoreNewestOnTop;
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
		elements.add(newElement);
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
		// Multisets.removeOccurrences(elements, outdatedElements);
		elements.removeAll(outdatedElements);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#evalute(de.uniol.inf.is.odysseus.core.
	 * collection.Tuple, de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] evalute(final T trigger, final PointInTime pointInTime) {
		boolean sameAsLastOutput = true;
		int i = 0;
		final List<T> topKList = new ArrayList<>(k);
		for (final Iterator<T> iter = elements.iterator(); iter.hasNext() && i < k;) {
			final T e = iter.next();
			if (i >= lastTopKOutput.size()) {
				sameAsLastOutput = false;
				lastTopKOutput.add(e);
			} else if (!e.equals(lastTopKOutput.get(i))) {
				sameAsLastOutput = false;
				lastTopKOutput.set(i, e);
			}
			topKList.add((T) e.clone());
			++i;
		}
		if (i < lastTopKOutput.size()) {
			sameAsLastOutput = false;
			for (int j = lastTopKOutput.size() - 1; j >= i; --j) {
				lastTopKOutput.remove(j);
			}
		}
		if (sameAsLastOutput) {
			return new Object[] { null };
		}
		return new Object[] { topKList };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * needsOrderedElements()
	 */
	@Override
	public boolean needsOrderedElements() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * getOutputAttributes()
	 */
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final SDFDatatype datatype = SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST, SDFDatatype.TUPLE,
				subSchema);
		return Collections.singleton(new SDFAttribute(null, outputAttributeNames[0], datatype, null, null, null));
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.factory.
	 * IAggregationFunctionFactory#createInstance(java.util.Map,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver)
	 */
	@Override
	public IAggregationFunction createInstance(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {
		final int k = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters, "TOP_K", -1);
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver, 0, false);
		String outputAttributeName = AggregationFunctionParseOptionsHelper.getFunctionParameterAsString(parameters,
				AggregationFunctionParseOptionsHelper.OUTPUT_ATTRIBUTES);
		final boolean newestOnTop = AggregationFunctionParseOptionsHelper.getFunctionParameterAsBoolean(parameters,
				"NEWEST_ON_TOP", true);
		if (outputAttributeName == null) {
			outputAttributeName = "TopK";
		}
		return new TopK<>(k, attributes, outputAttributeName, attributeResolver.getSchema().get(0), newestOnTop);
	}

}
