package de.uniol.inf.is.odysseus.finance.risk.var.model;

import java.util.List;

/**
 * 
 * @author Christoph Schröer
 *
 */
abstract public class NumericalVaRForecaster extends VaRForecaster {

	protected List<Double> sample;

	public NumericalVaRForecaster() {
		super();
	}

	public NumericalVaRForecaster(HistoricalSimulationVaRForecaster o) {
		super(o);
		this.sample = o.sample;
	}

	public List<Double> getSample() {
		return sample;
	}

	public void setSample(List<Double> sample) {
		this.sample = sample;
	}
}
