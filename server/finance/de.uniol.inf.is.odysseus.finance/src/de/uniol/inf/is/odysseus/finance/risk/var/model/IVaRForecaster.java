package de.uniol.inf.is.odysseus.finance.risk.var.model;

import org.apache.commons.math3.distribution.RealDistribution;

/**
 * This interface represents a Value-at-Risk-model.
 * 
 * @author Christoph Schröer
 *
 */
public interface IVaRForecaster {

	/**
	 * 
	 * @param confidenceLevel
	 * @param timeHorizon
	 * @return
	 */
	public double getVaR(double confidenceLevel, int timeHorizon);
	
	/**
	 * 
	 * @param confidenceLevel
	 * @param timeHorizon
	 * @param calculationMode
	 * @return
	 */
	public double getVaR(double confidenceLevel, int timeHorizon, CalculationMode calculationMode);

	/**
	 * 
	 * @param distribution
	 */
	public void setDistribution(RealDistribution distribution);

}
