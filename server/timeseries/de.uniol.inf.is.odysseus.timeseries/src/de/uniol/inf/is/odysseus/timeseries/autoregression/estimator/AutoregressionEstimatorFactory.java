package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.EWMAForecaster;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.GARCH11Forecaster;
import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;
import de.uniol.inf.is.odysseus.timeseries.logicaloperator.LearningMode;

/**
 * Factory to create Imputation(@see IImputation)-Objects.
 * 
 * @author Christoph Schröer
 *
 */
public class AutoregressionEstimatorFactory {

	LearningMode learningMode;

	public AutoregressionEstimatorFactory(LearningMode learningMode) {
		super();
		this.learningMode = learningMode;
	}

	public AbstractAutoregressionModelEstimator<Double> createEstimator(String modelName,
			Map<String, String> modelOptions) {

		if (this.learningMode == LearningMode.NON_LEARNING_MODE) {

			if (modelName.equalsIgnoreCase(GARCH11Forecaster.NAME)) {

				IAutoregressionForecaster<Double> parametrizedGARCHModel = new GARCH11Forecaster(
						Double.valueOf(modelOptions.get("alpha1")), Double.valueOf(modelOptions.get("beta1")),
						Double.valueOf(modelOptions.get("omega")));
				return new GARCHParametrizedNonEstimator(parametrizedGARCHModel);

			} else if (modelName.equalsIgnoreCase(EWMAForecaster.NAME)) {
				IAutoregressionForecaster<Double> parametrizedEWMAHModel = new EWMAForecaster(
						Double.valueOf(modelOptions.get("lambda")));
				return new GARCHParametrizedNonEstimator(parametrizedEWMAHModel);
			}

			throw new IllegalArgumentException("Model does not exist.");

		} else {
			throw new IllegalArgumentException("Estimation methods not implemented yet.");
		}

	}
}
