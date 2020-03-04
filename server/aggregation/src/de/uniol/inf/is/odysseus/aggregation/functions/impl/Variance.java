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
 * Incremental calculation of mean an variance.
 * 
 * <p>
 * TODO: Use an algo that is more numerical stable? See wiki link.
 * 
 * @see <a href="https://en.wikipedia.org/w/index.php?title=
 *      Algorithms_for_calculating_variance&oldid=707745388#Online_algorithm">
 *      https://en.wikipedia.org/w/index.php?title=
 *      Algorithms_for_calculating_variance&oldid=707745388#Online_algorithm</a>
 * 
 * 
 * 
 * @author Cornelius Ludmann
 *
 */
public class Variance<M extends ITimeInterval, T extends Tuple<M>> extends AbstractIncrementalAggregationFunction<M, T>
		implements IAggregationFunctionFactory {

	private static final long serialVersionUID = -5765741315313917471L;

	private long n;
	private final double[] mean;
	private final double[] m2;

	public Variance(final Variance<M, T> other) {
		super(other);
		this.n = 0;
		this.mean = new double[other.mean.length];
		Arrays.fill(mean, 0.0);
		this.m2 = new double[other.m2.length];
		Arrays.fill(m2, 0.0);
	}

	public Variance() {
		super();
		n = 0;
		mean = null;
		m2 = null;
	}

	public Variance(final int[] attributes, final String[] outputNames) {
		super(attributes, outputNames);
		this.n = 0;
		this.mean = new double[attributes.length];
		Arrays.fill(mean, 0.0);
		this.m2 = new double[attributes.length];
		Arrays.fill(m2, 0.0);
		if (outputNames.length != attributes.length) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public Variance(final int inputAttributesLength, final String[] outputNames) {
		super(null, outputNames);
		this.n = 0;
		this.mean = new double[inputAttributesLength];
		Arrays.fill(mean, 0.0);
		this.m2 = new double[inputAttributesLength];
		Arrays.fill(m2, 0.0);
		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
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
		++n;
		for (int i = 0; i < attr.length; ++i) {
			double x = 0;

			if (attr[i] != null) {
				x = ((Number) attr[i]).doubleValue();
			}

			final double delta = x - mean[i];
			mean[i] += delta / n;
			m2[i] += delta * (x - mean[i]);
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
			--n;
			for (int i = 0; i < attr.length; ++i) {
				double x = 0;

				if (attr[i] != null) {
					x = ((Number) attr[i]).doubleValue();
				}

				final double delta = x - mean[i];

				if (n < 1) {
					mean[i] = 0;
				} else {
					mean[i] -= delta / n;
				}

				m2[i] -= delta * (x - mean[i]);
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
		final Double[] result = new Double[m2.length];
		for (int i = 0; i < m2.length; ++i) {
			if (n < 2) {
				result[i] = Double.NaN;
			} else {
				result[i] = m2[i] / (n - 1);
			}
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
		final List<SDFAttribute> result = new ArrayList<>(m2.length);

		for (final String attr : outputAttributeNames) {
			result.add(new SDFAttribute(null, attr, SDFDatatype.DOUBLE, null, null, null));
		}
		return result;
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

		if (attributes == null) {
			return new Variance<>(attributeResolver.getSchema().get(0).size(), outputNames);
		}
		return new Variance<>(attributes, outputNames);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.aggregation.functions.
	 * AbstractIncrementalAggregationFunction#clone()
	 */
	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new Variance<>(this);
	}

}
