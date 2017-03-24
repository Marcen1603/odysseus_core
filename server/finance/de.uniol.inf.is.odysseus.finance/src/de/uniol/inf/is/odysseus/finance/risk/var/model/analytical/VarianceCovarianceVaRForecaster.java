package de.uniol.inf.is.odysseus.finance.risk.var.model.analytical;


import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.finance.risk.var.model.CalculationMode;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class VarianceCovarianceVaRForecaster extends AnalyticalVaRForecaster implements IClone {

	public static String NAME = "VarianceCovarianceForecaster";
	
	protected Double volatility;

	public VarianceCovarianceVaRForecaster(Double volatility) {
		super();
		this.volatility = volatility;
	}

	public VarianceCovarianceVaRForecaster(VarianceCovarianceVaRForecaster o) {
		super(o);
		this.volatility = o.volatility;
	}

	public Double getVariance() {
		return volatility;
	}

	@Override
	public double getVaR(double confidenceLevel, int timeHorizon) {
		return this.getVaR(confidenceLevel, timeHorizon, CalculationMode.DISTRIBUTION_MODE);
	}

	@Override
	public double getVaR(double confidenceLevel, int timeHorizon, CalculationMode calculationMode) {
		Double valueAtRisk = this.volatility * this.distribution.inverseCumulativeProbability(confidenceLevel);
		
		if (timeHorizon > 1) {
			valueAtRisk = this.getVaRByTimeHorizon(valueAtRisk, timeHorizon);
		}
		
		return valueAtRisk;
	}
	
	@Override
	public String toString() {
		return VarianceCovarianceVaRForecaster.NAME + ", Distribution: " + this.distribution.toString();
	}
	
	public IClone clone() {
		return new VarianceCovarianceVaRForecaster(this);
	}
	
}
