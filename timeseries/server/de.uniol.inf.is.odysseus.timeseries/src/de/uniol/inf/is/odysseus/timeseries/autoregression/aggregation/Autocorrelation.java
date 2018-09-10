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
package de.uniol.inf.is.odysseus.timeseries.autoregression.aggregation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Aggregation Function to calculate the autocorrelation on one attribute.
 * 
 * @author Christoph Schröer
 *
 */
public class Autocorrelation<M extends ITimeInterval, T extends Tuple<M>>
		extends AbstractIncrementalAggregationFunction<M, T>implements IAggregationFunctionFactory {

	public final static String LAG_PARAMETER_NAME = "lag";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6481641905388320088L;

	protected static Logger logger = LoggerFactory.getLogger(Autocorrelation.class);

	/**
	 * lag > 0
	 */
	private Integer lag;

	/**
	 * list with the original sample data. the key of HashMap is the index in
	 * inputAttributesIndices
	 */
	private HashMap<Integer, LinkedList<Double>> sampleValues;

	public Autocorrelation() {
		super();
		this.lag = 1;
		this.sampleValues = new HashMap<>();
	}

	public Autocorrelation(final int[] inputAttributesIndices, final String[] outputAttributeNames, int lag) {
		super(inputAttributesIndices, outputAttributeNames);
		this.lag = lag;
		this.sampleValues = new HashMap<>();

		//
		for (int i = 0; i < inputAttributesIndices.length; i++) {
			this.sampleValues.put(i, new LinkedList<>());
		}

	}

	public Autocorrelation(final int inputAttributesLength, final String[] outputNames, int lag) {
		super(null, outputNames);
		this.lag = lag;
		// this.sum = new Double[inputAttributesLength];
		// Arrays.fill(sum, 0.0);
		this.sampleValues = new HashMap<>();

		for (int i = 0; i < inputAttributesLength; i++) {
			this.sampleValues.put(i, new LinkedList<>());
		}

		if (outputNames.length != inputAttributesLength) {
			throw new IllegalArgumentException("Input attribute length is not equal output attribute length.");
		}
	}

	public Autocorrelation(Autocorrelation<M, T> autocorrelation) {
		super(autocorrelation);
		this.sampleValues = autocorrelation.sampleValues;
		this.lag = autocorrelation.lag;
	}

	@Override
	public void addNew(T newElement) {
		Object[] attr = getAttributes(newElement);

		for (int i = 0; i < attr.length; ++i) {
			if (attr[i] != null) {
				LinkedList<Double> iThLinkedList = this.sampleValues.get(i);
				if (attr[i] instanceof Double) {
					iThLinkedList.add((Double) attr[i]);
				} else {
					iThLinkedList.add(((Number) attr[i]).doubleValue());
				}
			}
		}
	}

	@Override
	public void removeOutdated(Collection<T> outdatedElements, T trigger, PointInTime pointInTime) {

		for (final T e : outdatedElements) {
			final Object[] attr = getAttributes(e);
			for (int i = 0; i < attr.length; ++i) {
				if (attr[i] != null) {
					LinkedList<Double> iThLinkedList = this.sampleValues.get(i);
					iThLinkedList.removeFirst();
				}
			}
		}

	}

	@Override
	public Object[] evalute(T trigger, PointInTime pointInTime) {

		Object[] attr = getAttributes(trigger);
		final Double[] result = new Double[attr.length];
		for (int i = 0; i < attr.length; ++i) {
			if (attr[i] != null) {
				LinkedList<Double> iThLinkedList = this.sampleValues.get(i);

				if (iThLinkedList.size() > (this.lag+1)) {
					List<Double> xList = iThLinkedList.subList(0, iThLinkedList.size() - this.lag);
					Double[] xValues = new Double[xList.size()];
					int indexX = 0;
					for (Double x : xList) {
						xValues[indexX] = x;
						indexX++;
					}
					double[] xValuesDouble = Stream.of(xValues).mapToDouble(Double::doubleValue).toArray();

					List<Double> yList = iThLinkedList.subList(this.lag, iThLinkedList.size());
					Double[] yValues = new Double[yList.size()];
					int indexY = 0;
					for (Double y : yList) {
						yValues[indexY] = y;
						indexY++;
					}

					double[] yValuesDouble = Stream.of(yValues).mapToDouble(Double::doubleValue).toArray();

					PearsonsCorrelation corr = new PearsonsCorrelation();
					Double autocorr = corr.correlation(xValuesDouble, yValuesDouble);
					result[i] = autocorr;
				}
			}
		}

		return result;
	}

	@Override
	public Collection<SDFAttribute> getOutputAttributes() {

		final List<SDFAttribute> result = new ArrayList<>(outputAttributeNames.length);

		for (final String attr : outputAttributeNames) {
			result.add(new SDFAttribute(null, attr, SDFDatatype.DOUBLE, null, null, null));
		}
		return result;
	}

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		final boolean checkInputOutputLength = AggregationFunctionParseOptionsHelper
				.checkInputAttributesLengthEqualsOutputAttributesLength(parameters, attributeResolver);
		final boolean checkNumericInput = AggregationFunctionParseOptionsHelper.checkNumericInput(parameters,
				attributeResolver);
		return checkInputOutputLength && checkNumericInput;

	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {

		// get index of input attributes
		// INPUT_ATTRIBUTES, for which the autocorrelation should be calculate
		int[] inputAttributesIndices = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver);

		final String[] outputAttributesNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		int lag = AggregationFunctionParseOptionsHelper.getFunctionParameterAsInt(parameters,
				Autocorrelation.LAG_PARAMETER_NAME, 1);

		if (inputAttributesIndices == null) {
			return new Autocorrelation<>(attributeResolver.getSchema().get(0).size(), outputAttributesNames, lag);
		}

		// create the aggregation function
		Autocorrelation<M, T> autocorrelationFunction = new Autocorrelation<>(inputAttributesIndices,
				outputAttributesNames, lag);

		return autocorrelationFunction;
	}

	@Override
	public AbstractIncrementalAggregationFunction<M, T> clone() {
		return new Autocorrelation<>(this);
	}

}
