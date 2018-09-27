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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

/**
 * @author Cornelius Ludmann
 *
 */
public class Sum<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -2434803583219206999L;

	protected final Double[] sum;

	public Sum(final Sum<M, T> other) {
		super(other);
		this.sum = new Double[other.sum.length];
		Arrays.fill(sum, 0.0);
	}

	public Sum() {
		super();
		sum = null;
	}

	public Sum(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		this.sum = new Double[attributes.length];
		Arrays.fill(sum, 0.0);
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public Sum(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		this.sum = new Double[inputAttributesLength];
		Arrays.fill(sum, 0.0);
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#addNewAndEvaluate(de.uniol.inf.is.
	 * odysseus.core.collection.Tuple)
	 */
	@Override
	public Object[] addNewAndEvaluate(final T newElement) {
		addNew(newElement);
		return this.sum;
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
				if (attr[i] instanceof Double) {
					this.sum[i] += ((Double) attr[i]);
				} else {
					this.sum[i] += ((Number) attr[i]).doubleValue();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * IIncrementalAggregationFunction#removeOutdatedAndEvaluate(java.util.
	 * Collection, de.uniol.inf.is.odysseus.core.collection.Tuple,
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Object[] removeOutdatedAndEvaluate(final Collection<T> outdatedElements, final T trigger,
			final PointInTime pointInTime) {
		removeOutdated(outdatedElements, trigger, pointInTime);
		return this.sum;
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
					if (attr[i] instanceof Double) {
						this.sum[i] -= ((Double) attr[i]);
					} else {
						this.sum[i] -= ((Number) attr[i]).doubleValue();
					}
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
		return this.sum;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * checkParameters(java.util.Map,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver)
	 */
	@Override
	public boolean checkParameters(final Map<String, Object> parameters, final IAttributeResolver attributeResolver) {
		final boolean checkInputOutputLength = AggregationFunctionParseOptionsHelper
				.checkInputAttributesLengthEqualsOutputAttributesLength(parameters,
				attributeResolver);
		final boolean checkNumericInput = AggregationFunctionParseOptionsHelper.checkNumericInput(parameters,
				attributeResolver);
		return checkInputOutputLength && checkNumericInput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * createParametrizedCopy(java.util.Map,
	 * de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver)
	 */
	@Override
	public IAggregationFunction createInstance(final Map<String, Object> parameters,
			final IAttributeResolver attributeResolver) {

		final int[] attributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters, attributeResolver);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		if (attributes == null) {
			return new Sum<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}
		return new Sum<>(attributes, outputNames);
	}



	/**
	 * @param parameters
	 * @param attributeResolver
	 * @return
	 */


	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction#
	 * getOutputAttributes()
	 */
	@Override
	public Collection<SDFAttribute> getOutputAttributes() {
		final List<SDFAttribute> result = new ArrayList<>(sum.length);

		for (final String attr : outputAttributeNames) {
				result.add(new SDFAttribute(null, attr, SDFDatatype.DOUBLE, null, null, null));
			}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractIncrementalAggregationFunction#clone()
	 */
	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new Sum<M, T>(this);
	}

}
