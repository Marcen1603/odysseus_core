package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import java.util.Map;

import de.uniol.inf.is.odysseus.timeseries.autoregression.model.EWMAForecaster;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.GARCH11Forecaster;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * Factory to create {@link IAutoregressionEstimator}-Objects.
 * 
 * @author Christoph Schröer
 *
 */
public class AutoregressionEstimatorFactory {

	final private EstimationMode estimationMode;

	public AutoregressionEstimatorFactory(EstimationMode estimationMode) {
		super();
		this.estimationMode = estimationMode;
	}

	public AbstractAutoregressionModelEstimator<Double> createEstimator(String modelName,
			Map<String, String> modelOptions) {

		if (this.estimationMode == EstimationMode.NON_ESTIMATING) {

			if (modelName.equalsIgnoreCase(GARCH11Forecaster.NAME)) {

				IAutoregressionForecaster<Double> parametrizedGARCHModel = new GARCH11Forecaster(
						Double.valueOf(modelOptions.get("alpha1")), Double.valueOf(modelOptions.get("beta1")),
						Double.valueOf(modelOptions.get("omega")));
				return new ParametrizedGARCHEstimator(parametrizedGARCHModel);

			} else if (modelName.equalsIgnoreCase(EWMAForecaster.NAME)) {
				IAutoregressionForecaster<Double> parametrizedEWMAHModel = new EWMAForecaster(
						Double.valueOf(modelOptions.get("lambda")));
				return new ParametrizedGARCHEstimator(parametrizedEWMAHModel);
			}

			throw new IllegalArgumentException("Model does not exist. Possible values are e. g.: "
					+ GARCH11Forecaster.NAME + ", " + EWMAForecaster.NAME);

		} else {
			throw new IllegalArgumentException("Estimation methods not implemented yet.");
		}

	}
}
