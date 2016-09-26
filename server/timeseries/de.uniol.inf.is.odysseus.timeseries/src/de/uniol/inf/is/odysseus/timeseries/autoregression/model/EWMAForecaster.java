package de.uniol.inf.is.odysseus.timeseries.autoregression.model;

import java.util.LinkedList;

/**
 * Special GARCH(1,1) with p = 1 and q = 1
 * 
 * and EWMA weights.
 * 
 * @author Christoph Schröer
 *
 */
public class EWMAForecaster extends GARCHForecaster {

	/**
	 * Model Name (also for e.g. logical operator)
	 */
	public static final String NAME = "EWMA";
	
	public EWMAForecaster(Double lambda) {
		super(1, 1, new LinkedList<Double>(), new LinkedList<Double>(), 0.0);
		this.alphas.addLast((1 - lambda));
		this.betas.addLast(lambda);
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