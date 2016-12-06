package de.uniol.inf.is.odysseus.timeseries.autoregression.model;

import java.util.LinkedList;

/**
 * 
 * Exponentially weight moving average.
 * 
 * This is a special GARCH(1,1) with p = 1 and q = 1 and weight lambda and (1-lambda).
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
	 * @param lastLagResidual
	 * @param lastLagVariance
	 * @return
	 */
	public Double forecast(Double lastLagResidual, Double lastLagVariance) {
		LinkedList<Double> lagResiduals = new LinkedList<Double>();
		lagResiduals.addLast(lastLagResidual);
		LinkedList<Double> lagVariances = new LinkedList<Double>();
		lagVariances.addLast(lastLagVariance);
		return super.forecast(lagResiduals, lagVariances);
	}

}