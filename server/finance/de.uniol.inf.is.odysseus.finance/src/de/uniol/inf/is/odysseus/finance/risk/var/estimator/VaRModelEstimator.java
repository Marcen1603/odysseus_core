package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;

/**
 * 
 * A VaRModelEstimator estimates and creates a Value-at-Risk model for
 * forecasting.
 * 
 * @author Christoph Schröer
 *
 */
abstract public class VaRModelEstimator implements IVaRModelEstimator {

	protected IVaRForecaster varForecaster;

	protected boolean isEstimated;

	public VaRModelEstimator() {
		this.isEstimated = false;
	}

	public boolean isEstimated() {
		return this.isEstimated;
	}

	public IVaRForecaster getModel() {
		return this.varForecaster;
	}

	public IVaRForecaster getModel(boolean estimate) {
		if (estimate) {
			this.estimateModel();
		}
		return this.getModel();
	}
}
