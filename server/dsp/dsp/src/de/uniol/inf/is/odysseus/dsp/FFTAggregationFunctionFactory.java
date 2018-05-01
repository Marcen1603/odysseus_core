package de.uniol.inf.is.odysseus.dsp;

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public class FFTAggregationFunctionFactory implements IAggregationFunctionFactory {

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
		final int[] inputAttributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver, 0, false);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);

		return new FFT<>(inputAttributes, outputNames);
	}

	@Override
	public String getFunctionName() {
		return "FFT";
	}

}
