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
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Cornelius Ludmann
 *
 */
public abstract class AbstractNest<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T>
		implements INonIncrementalAggregationFunction<M, T>, IAggregationFunctionFactory {

	private static final long serialVersionUID = 7516234480388507632L;

	protected final Collection<T> elements;
	protected SDFSchema subSchema;
	protected final boolean preserveOrderingOfElements;
	protected final boolean sortElements;

	public AbstractNest() {
		super();
		elements = null;
		this.preserveOrderingOfElements = false;
		this.sortElements = false;
	}

	/**
	 * Uses incremental version that restricts to attributes.
	 * 
	 * @param elements
	 * @param attributes
	 * @param outputAttributeName
	 * @param subSchema
	 * @param preserveOrderingOfElements
	 */
	public AbstractNest(final Collection<T> elements, final int[] attributes, final String outputAttributeName,
			final SDFSchema subSchema,
 final boolean preserveOrderingOfElements, final boolean sortElements) {
		super(attributes, new String[] { outputAttributeName });
		this.subSchema = subSchema.clone();
		this.elements = elements;
		this.preserveOrderingOfElements = preserveOrderingOfElements;
		this.sortElements = sortElements;
	}

	/**
	 * Uses non-incremental version that uses all attributes.
	 * 
	 * @param outputAttributeName
	 * @param subSchema
	 * @param preserveOrderingOfElements
	 */
	public AbstractNest(final String outputAttributeName, final SDFSchema subSchema,
			final boolean preserveOrderingOfElements, final boolean sortElements) {
		super(null, new String[] { outputAttributeName });
		this.subSchema = subSchema.clone();
		this.elements = null;
		this.preserveOrderingOfElements = preserveOrderingOfElements;
		this.sortElements = sortElements;
	}

	protected AbstractNest(final AbstractNest<M, T> other, final Collection<T> elements) {
		super(other);
		this.subSchema = other.subSchema.clone();
		this.preserveOrderingOfElements = other.preserveOrderingOfElements;
		this.sortElements = other.sortElements;
		this.elements = elements;
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
		elements.add(this.getAttributesAsTuple(newElement));
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
		elements.removeIf(e -> e.getMetadata().getEnd().beforeOrEquals(pointInTime));
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
		List<T> result;
		if (this.elements instanceof ArrayList) {
			result = (List<T>) ((ArrayList<T>) elements).clone();
		} else if (this.elements instanceof LinkedList) {
			result = (List<T>) ((LinkedList<T>) elements).clone();
		} else {
			result = new ArrayList<>(elements);
		}
		// if (this.elements instanceof HashSet) {
		// return new Object[] { ((HashSet<T>) elements).clone() };
		// }

		if (sortElements) {
			Collections.sort(result);
		}

		return new Object[] { result };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * needsOrderedElements()
	 */
	@Override
	public boolean needsOrderedElements() {
		return preserveOrderingOfElements;
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
	 * INonIncrementalAggregationFunction#evaluate(java.util.Collection,
	 * de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object[] evaluate(final Collection<T> elements, final T trigger, final PointInTime pointInTime) {
		if (inputAttributeIndices == null) {
			List<T> result;
			if (this.elements instanceof ArrayList) {
				result = (List<T>) ((ArrayList<T>) elements).clone();
			} else if (this.elements instanceof LinkedList) {
				result = (List<T>) ((LinkedList<T>) elements).clone();
			} else {
				result = new ArrayList<>(elements);
			}
			if (sortElements) {
				Collections.sort(result);
			}
			return new Object[] { result };
		} else {
			// Use better an incremental implementation!?
			final List<T> results = new ArrayList<>(elements.size());
			for (final T e : elements) {
				results.add(this.getAttributesAsTuple(e));
			}
			if (sortElements) {
				Collections.sort(results);
			}
			return new Object[] { results };
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractIncrementalAggregationFunction#isIncremental()
	 */
	@Override
	public boolean isIncremental() {
		return inputAttributeIndices != null;
	}
}
