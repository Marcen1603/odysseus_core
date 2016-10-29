package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;

public class VaRModelEstimatorFactory {

	final private Map<String, Object> parameters;
	final private IAttributeResolver attributeResolver;

	// TODO TupleSchemaHelper??
	public VaRModelEstimatorFactory(final Map<String, Object> parameters, final IAttributeResolver attributeResolver) {
		super();
		this.parameters = parameters;
		this.attributeResolver = attributeResolver;
	}

	public IVaRModelEstimator createEstimator(String estimatorName, Map<String, String> estimatorOptions) {

		if (estimatorName.equalsIgnoreCase(ReturnHistoricalSimulationEstimator.NAME)) {

			int[] returnInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, "return_attribute");

			int returnIndex = returnInputAttributesIndices[0];

			IVaRModelEstimator estimator = new ReturnHistoricalSimulationEstimator(returnIndex);
			return estimator;

		} else if (estimatorName.equalsIgnoreCase(VarianceIntegratedHistoricalSimulationEstimator.NAME)) {

			int[] returnInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, "return_attribute");

			int[] varianceInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, "variance_attribute");

			int returnIndex = returnInputAttributesIndices[0];

			int varianceIndex = varianceInputAttributesIndices[0];

			IVaRModelEstimator estimator = new VarianceIntegratedHistoricalSimulationEstimator(returnIndex,
					varianceIndex);

			return estimator;
		}

		throw new IllegalArgumentException("Estimator does not exist.");

	}

}
