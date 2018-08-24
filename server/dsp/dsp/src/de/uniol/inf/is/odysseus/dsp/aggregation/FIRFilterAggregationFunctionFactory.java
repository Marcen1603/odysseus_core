package de.uniol.inf.is.odysseus.dsp.aggregation;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public class FIRFilterAggregationFunctionFactory implements IAggregationFunctionFactory {

	public static final String COEFFICIENTS = "COEFFICIENTS";

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final boolean checkInputOutputLength = AggregationFunctionParseOptionsHelper
				.checkInputAttributesLengthEqualsOutputAttributesLength(parameters, attributeResolver);
		final boolean checkNumericInput = AggregationFunctionParseOptionsHelper.checkNumericInput(parameters,
				attributeResolver);
		final Object coefficientsParameter = AggregationFunctionParseOptionsHelper.getFunctionParameter(parameters,
				COEFFICIENTS);
		final boolean checkCoefficientsParameter = coefficientsParameter != null
				&& coefficientsParameter instanceof List && ((List<?>) coefficientsParameter).size() > 0
				&& ((List<?>) coefficientsParameter).stream().allMatch(value -> value instanceof Number);
		return checkInputOutputLength && checkNumericInput && checkCoefficientsParameter;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int[] inputAttributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters,
				attributeResolver, 0, false);
		final String[] outputNames = AggregationFunctionParseOptionsHelper.getOutputAttributeNames(parameters,
				attributeResolver);
		final List<?> coefficientsParameter = (List<?>) AggregationFunctionParseOptionsHelper
				.getFunctionParameter(parameters, COEFFICIENTS);
		return new FIRFilter<>(inputAttributes, outputNames,
				coefficientsParameter.stream().mapToDouble(value -> ((Number) value).doubleValue()).toArray());
	}

	@Override
	public String getFunctionName() {
		return "FIR";
	}

}
