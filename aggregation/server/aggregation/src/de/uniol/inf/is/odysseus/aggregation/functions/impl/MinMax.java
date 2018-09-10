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
import java.util.Map;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.aggregation.functions.AbstractIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Cornelius Ludmann
 *
 */
public class MinMax<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -2798765541854593140L;

	protected PriorityQueue<Object>[] pq;

	private final Collection<SDFAttribute> outputAttributes;

	private final boolean isMinFunction;

	public MinMax() {
		super();
		pq = null;
		outputAttributes = null;
		isMinFunction = false;
	}

	public MinMax(final boolean min) {
		super();
		pq = null;
		outputAttributes = null;
		isMinFunction = min;
	}

	@SuppressWarnings("unchecked")
	public MinMax(final MinMax<M, T> other) {
		super(other);
		pq = new PriorityQueue[other.pq.length];
		for (int i = 0; i < other.pq.length; ++i) {
			if (other.isMinFunction) {
				pq[i] = new PriorityQueue<>();
			} else {
				pq[i] = new PriorityQueue<>(Collections.reverseOrder());
			}
		}
		this.isMinFunction = other.isMinFunction;
		this.outputAttributes = other.outputAttributes;
	}

	@SuppressWarnings("unchecked")
	public MinMax(final int attributesLength, final String[] outputAttrNames,
			final Collection<SDFAttribute> outputAttributes, final boolean min) {
		super(null, outputAttrNames);
		pq = new PriorityQueue[attributesLength];
		for (int i = 0; i < attributesLength; ++i) {
			if (min) {
				pq[i] = new PriorityQueue<>();
			} else {
				pq[i] = new PriorityQueue<>(Collections.reverseOrder());
			}
		}
		this.isMinFunction = min;
		this.outputAttributes = outputAttributes;
	}

	@SuppressWarnings("unchecked")
	public MinMax(final int[] attributes, final String[] outputAttrNames,
			final Collection<SDFAttribute> outputAttributes, final boolean min) {
		super(attributes, outputAttrNames);
		pq = new PriorityQueue[attributes.length];
		for (int i = 0; i < attributes.length; ++i) {
			if (min) {
				pq[i] = new PriorityQueue<>();
			} else {
				pq[i] = new PriorityQueue<>(Collections.reverseOrder());
			}
		}
		this.isMinFunction = min;
		this.outputAttributes = outputAttributes;
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
		final Object[] attr = getAttributes(newElement);
		for (int i = 0; i < attr.length; ++i) {
			if (attr[i] != null) {
				this.pq[i].offer(attr[i]);
			}
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
		for (final T e : outdatedElements) {
			final Object[] attr = getAttributes(e);
			for (int i = 0; i < attr.length; ++i) {
				if (attr[i] != null) {
					this.pq[i].remove(attr[i]);
				}
			}
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
		final Object[] result = new Object[pq.length];
		for (int i = 0; i < pq.length; ++i) {
			result[i] = pq[i].peek();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * getOutputAttributes()
	 */
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		return outputAttributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractIncrementalAggregationFunction#clone()
	 */
	@Override
	public MinMax<M, T> clone() {
		return new MinMax<M, T>(this);
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
		// TODO Auto-generated method stub
		return true;
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
		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);
		final String functionName = AggregationFunctionParseOptionsHelper.getFunctionName(parameters);
		final boolean isMin = "min".equalsIgnoreCase(functionName);

		if (attributes == null) {
			final SDFSchema schema = attributeResolver.getSchema().get(0);
			final Collection<SDFAttribute> outputAttr = new ArrayList<>();
			int i = 0;
			for (final SDFAttribute attr : schema) {
				outputAttr.add(new SDFAttribute(null, outputNames[i], attr.getDatatype(), null, null, null));
				++i;
			}
			return new MinMax<>(schema.size(), outputNames, outputAttr, isMin);
		} else {
			final SDFSchema schema = attributeResolver.getSchema().get(0);
			final Collection<SDFAttribute> outputAttr = new ArrayList<>();
			for (int i = 0; i < attributes.length; ++i) {
				outputAttr.add(
						new SDFAttribute(null, outputNames[i], schema.getAttribute(attributes[i]).getDatatype(), null,
								null, null));
				++i;
			}
			return new MinMax<>(attributes, outputNames, outputAttr, isMin);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractAggregationFunction#getFunctionName()
	 */
	@Override
	public String getFunctionName() {
		if(isMinFunction) {
			return "Min";
		} else {
			return "Max";
		}
	}

}
