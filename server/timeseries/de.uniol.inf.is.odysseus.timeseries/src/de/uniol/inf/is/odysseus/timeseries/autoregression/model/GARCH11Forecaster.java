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

	@Override
	public Double forecast(LinkedList<Double> residuals, Integer timeHorizon) {
		if (timeHorizon == 1) {
			return this.forecast(residuals);
		} else if (timeHorizon > 1) {
			Double sigma_tPlus1 = super.forecast(residuals);

			Double sigma_tPlusTimeHorizon = this.longTermVariance
					+ Math.pow(this.alphas.getFirst() + this.betas.getFirst(), timeHorizon)
							* (sigma_tPlus1 - this.longTermVariance);

			return sigma_tPlusTimeHorizon;
		}

		throw new IllegalArgumentException("Time Horizon must be postive.");
	}

	@Override
	public Double forecast(LinkedList<Double> lagResiduals, LinkedList<Double> lagVariances, Integer timeHorizon) {
		if (timeHorizon == 1) {
			return this.forecast(lagResiduals, lagVariances);
		} else if (timeHorizon > 1) {
			Double sigma_tPlus1 = super.forecast(lagResiduals, lagVariances);

			Double sigma_tPlusTimeHorizon = this.longTermVariance
					+ Math.pow(this.alphas.getFirst() + this.betas.getFirst(), timeHorizon)
							* (sigma_tPlus1 - this.longTermVariance);

			return sigma_tPlusTimeHorizon;
		}

		throw new IllegalArgumentException("Time Horizon must be postive.");

	}

}