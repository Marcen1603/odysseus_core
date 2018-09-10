package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import java.util.Map;

import de.uniol.inf.is.odysseus.aggregation.functions.factory.AggregationFunctionParseOptionsHelper;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.finance.risk.var.aggregation.ModelValueAtRisk;
import de.uniol.inf.is.odysseus.finance.risk.var.estimator.analytical.VarianceCovarianceEstimator;

/**
 * Factory to create an instance of {@link IVaRModelEstimator}
 * 
 * @author Christoph Schröer
 *
 */
public class VaRModelEstimatorFactory {

	final private Map<String, Object> parameters;
	final private IAttributeResolver attributeResolver;

	public VaRModelEstimatorFactory(final Map<String, Object> parameters, final IAttributeResolver attributeResolver) {
		super();
		this.parameters = parameters;
		this.attributeResolver = attributeResolver;
	}

	public IVaRModelEstimator createEstimator(String estimatorName, Map<String, String> estimatorOptions) {

		if (estimatorName.equalsIgnoreCase(ReturnHistoricalSimulationEstimator.NAME)) {

			int[] returnInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, ModelValueAtRisk.RETURN_ATTR_PARAM_NAME);

			int returnIndex = returnInputAttributesIndices[0];

			IVaRModelEstimator estimator = new ReturnHistoricalSimulationEstimator(returnIndex);
			return estimator;

		} else if (estimatorName.equalsIgnoreCase(VolatilityIntegratedHistoricalSimulationEstimator.NAME)) {

			int[] returnInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, ModelValueAtRisk.RETURN_ATTR_PARAM_NAME);

			int[] volatilityInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, ModelValueAtRisk.VOLATILITY_ATTR_PARAM_NAME);

			int returnIndex = returnInputAttributesIndices[0];

			int volatilityIndex = volatilityInputAttributesIndices[0];

			IVaRModelEstimator estimator = new VolatilityIntegratedHistoricalSimulationEstimator(returnIndex,
					volatilityIndex);

			return estimator;
		} else if (estimatorName.equalsIgnoreCase(VarianceCovarianceEstimator.NAME)) {

			int[] varianceInputAttributesIndices = AggregationFunctionParseOptionsHelper.getAttributeIndices(parameters,
					attributeResolver, ModelValueAtRisk.VOLATILITY_ATTR_PARAM_NAME);
			int varianceIndex = varianceInputAttributesIndices[0];

			IVaRModelEstimator estimator = null;
			if (estimatorOptions.containsKey("distribution_name")) {
				String distributionName = estimatorOptions.get("distribution_name");
				estimator = new VarianceCovarianceEstimator(distributionName, varianceIndex);
			} else {
				estimator = new VarianceCovarianceEstimator(varianceIndex);
			}
			return estimator;

		}

		throw new IllegalArgumentException("Estimator does not exist.");

	}

}
