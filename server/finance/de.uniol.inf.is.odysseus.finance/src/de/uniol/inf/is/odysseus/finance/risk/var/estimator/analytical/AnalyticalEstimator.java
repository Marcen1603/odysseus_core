package de.uniol.inf.is.odysseus.finance.risk.var.estimator.analytical;

import de.uniol.inf.is.odysseus.finance.risk.var.estimator.VaRModelEstimator;

/**
 * 
 * A AnalyticalEstimator can estimate without a discrete sample of data. Those
 * Estimator makes assumptions of the distribution of the data.
 * 
 * @author Christoph Schröer
 *
 */
abstract public class AnalyticalEstimator extends VaRModelEstimator {

	final protected String distributionName;

	public AnalyticalEstimator(String distributionName) {
		super();
		this.distributionName = distributionName;
	}

	public AnalyticalEstimator() {
		super();
		this.distributionName = "NormalDistribution";
	}

}
