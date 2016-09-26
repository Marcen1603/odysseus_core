package de.uniol.inf.is.odysseus.timeseries.autoregression.model;

import java.util.LinkedList;

/**
 * Special GARCH(1,1) with p = 1 and q = 1
 * 
 * @author Christoph Schröer
 *
 */
public class GARCH11Forecaster extends GARCHForecaster {

	/**
	 * Model Name (also for e.g. logical operator)
	 */
	public static final String NAME = "GARCH11";
	
	public GARCH11Forecaster(Double alpha1, Double beta1, Double omega) {
		super(1, 1, new LinkedList<Double>(), new LinkedList<Double>(), omega);
		this.alphas.addLast(alpha1);
		this.betas.addLast(beta1);
	}

	/**
	 * 
	 * @param residual
	 * @param variance
	 * @return
	 */
	public Double forecast(Double residual, Double variance) {
		LinkedList<Double> lagResiduals = new LinkedList<Double>();
		lagResiduals.addLast(residual);
		LinkedList<Double> lagVariances = new LinkedList<Double>();
		lagVariances.addLast(variance);
		return super.forecast(lagResiduals, lagVariances);
	}

}