package de.uniol.inf.is.odysseus.finance.risk.var.estimator;

import de.uniol.inf.is.odysseus.finance.risk.var.model.IVaRForecaster;

/**
 * 
 * @author Christoph Schröer
 *
 */
public interface IVaRModelEstimator {

	public boolean isEstimated();

	public void estimateModel();

	public IVaRForecaster getModel();

	public IVaRForecaster getModel(boolean estimate);

}
