package de.uniol.inf.is.odysseus.timeseries.autoregression.estimator;

import de.uniol.inf.is.odysseus.timeseries.autoregression.model.IAutoregressionForecaster;

/**
 * 
 * This is a GARCH, which does not estimate. It has only a parameterized model.
 * 
 * @author Christoph Schröer
 *
 */
public class ParametrizedGARCHEstimator extends AbstractAutoregressionModelEstimator<Double> {

	IAutoregressionForecaster<Double> parametrizedGARCHModel;

	public ParametrizedGARCHEstimator(IAutoregressionForecaster<Double> parametrizedGARCHModel) {
		super();
		this.parametrizedGARCHModel = parametrizedGARCHModel;
	}

	@Override
	public void estimateModel() {
		this.isModelUpToDate = true;
	}

	@Override
	public IAutoregressionForecaster<Double> getModel(boolean estimate) {
		return this.parametrizedGARCHModel;
	}

}
