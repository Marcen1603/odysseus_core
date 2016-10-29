package de.uniol.inf.is.odysseus.finance.risk.var.model;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * 
 * @author Christoph Schröer
 *
 */
abstract public class VaRForecaster implements IVaRForecaster {

	protected RealDistribution distribution;

	public VaRForecaster(HistoricalSimulationVaRForecaster o) {
		this.distribution = o.distribution;
	}

	public VaRForecaster() {
	}

	@Override
	public void setDistribution(RealDistribution distribution) {
		this.distribution = distribution;
	}
	
}
