package de.uniol.inf.is.odysseus.finance.risk.var.model;

import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class HistoricalSimulationVaRForecaster extends NumericalVaRForecaster implements IClone {

	public static String NAME = "HistoricalSimulationVaRForecaster";

	public HistoricalSimulationVaRForecaster() {
		super();
	}

	public HistoricalSimulationVaRForecaster(HistoricalSimulationVaRForecaster o) {
		super(o);
	}

	@Override
	public double getVaR(double confidenceLevel, int timeHorizon) {

		return this.getVaR(confidenceLevel, timeHorizon, CalculationMode.SAMPLE_MODE);

	}

	@Override
	public String toString() {
		return HistoricalSimulationVaRForecaster.NAME + ", Distribution: " + this.distribution.toString()
				+ ", SampleSize: " + this.sample.size();
	}

	public IClone clone() {
		return new HistoricalSimulationVaRForecaster(this);
	}

	@Override
	public double getVaR(double confidenceLevel, int timeHorizon, CalculationMode calculationMode) {

		double valueAtRisk = 0.0;

		switch (calculationMode) {
		case DISTRIBUTION_MODE:
			valueAtRisk = this.getVaRByDistribution(confidenceLevel);
			break;

		case SAMPLE_MODE:
		default:
			valueAtRisk = this.getVaRBySample(confidenceLevel);

			break;
		}

		if (timeHorizon > 1) {
			valueAtRisk = this.getVaRByTimeHorizon(valueAtRisk, timeHorizon);
		}

		return valueAtRisk;

	}

	private double getVaRByDistribution(double confidenceLevel) {
		return this.distribution.inverseCumulativeProbability(confidenceLevel);
	}

	private double getVaRBySample(double confidenceLevel) {
		this.sample.sort(new Comparator<Double>() {

			@Override
			public int compare(Double o1, Double o2) {
				return o1.compareTo(o2);
			}
		});

		double indexDouble = this.sample.size() * confidenceLevel;

		int index = (int) Math.floor(indexDouble);

		// first for timehorizon 1
		return this.sample.get(index);
	}

}
