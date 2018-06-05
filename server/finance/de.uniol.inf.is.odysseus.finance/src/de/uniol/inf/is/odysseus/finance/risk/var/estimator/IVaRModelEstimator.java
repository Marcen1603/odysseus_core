package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;

/**
 * A VaRModelEstimator estimates and creates a Value-at-Risk model for
 * forecasting.
 * 
 * @author Christoph Schröer
 *
 */
public interface IVaRModelEstimator {

	/**
	 * 
	 * @return
	 * 		if the model is estimated
	 */
	public boolean isEstimated();

	/**
	 * Estimating a {@link IVaRForecaster}
	 */
	public void estimateModel();

	public IVaRForecaster getModel();

	public IVaRForecaster getModel(boolean estimate);

}
