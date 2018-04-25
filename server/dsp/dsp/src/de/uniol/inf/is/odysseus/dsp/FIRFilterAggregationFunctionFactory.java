package de.uniol.inf.is.odysseus.dsp;

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public class FIRFilterAggregationFunctionFactory implements IAggregationFunctionFactory {

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		final int[] inputAttributes = AggregationFunctionParseOptionsHelper.getInputAttributeIndices(parameters, attributeResolver);
		return new FIRFilter<>(inputAttributes);
	}

	@Override
	public String getFunctionName() {
		return "FIR";
	}

}
