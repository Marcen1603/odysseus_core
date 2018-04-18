package de.uniol.inf.is.odysseus.dsp;

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.IAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.factory.IAggregationFunctionFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public class ConstAggregationFunctionFactory implements IAggregationFunctionFactory  {

	@Override
	public boolean checkParameters(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return true;
	}

	@Override
	public IAggregationFunction createInstance(Map<String, Object> parameters, IAttributeResolver attributeResolver) {
		return new Const<>();
	}

	@Override
	public String getFunctionName() {
		return "Const";
	}

}
