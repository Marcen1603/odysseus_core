package de.uniol.inf.is.odysseus.timeseries.autoregression.model;

import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * 
 * This is the GARCH-Model for forecasting variances or other heteroskedastic
 * variables.
 * 
 * The used vocabulary (like q and p variable names) is noun in the original
 * paper of BOLLERSLEV (1986)
 * 
 * @author Christoph Schröer
 *
 */
public class GARCHForecaster implements IAutoregressionForecaster<Double> {

	protected static Logger logger = LoggerFactory.getLogger(GARCHForecaster.class);
	
	/**
	 * Number of residuals. also called number of autoregressive parameter
	 */
	final protected Integer q;

	/**
	 * Number of GARCH-Parameter, the conditional variances, also called number
	 * of moving average-parameters
	 */
	final protected Integer p;

	/**
	 * Parameter for the residuals.
	 */
	final protected LinkedList<Double> alphas;

	/**
	 * Parameter for the GARCH-Parameter
	 */
	final protected LinkedList<Double> betas;

	/**
	 * first parameter, in BOLLERSLEV is called alpha_0
	 */
	final protected Double omega;

	/**
	 * also called unconditional variance.
	 */
	protected Double longTermVariance;

	public GARCHForecaster(Integer p, Integer q, LinkedList<Double> alphas, LinkedList<Double> betas, Double omega) {
		super();
		this.q = q;
		this.p = p;
		this.alphas = alphas;
		this.betas = betas;
		this.omega = omega;

		// Calculating the longTermVariance
		Double alphaSum = this.alphas.stream().mapToDouble(Double::intValue).sum();
		Double betaSum = this.betas.stream().mapToDouble(Double::intValue).sum();
		if (!Double.isNaN(alphaSum) && !Double.isNaN(betaSum)) {
			this.longTermVariance = (this.omega) / (1 - (alphaSum + betaSum));
		}
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = true;
		if (obj instanceof GARCHForecaster) {

			if (!(((GARCHForecaster) obj).getP() == this.getP())) {
				isEqual = false;
			} else if (!(((GARCHForecaster) obj).getQ() == this.getQ())) {
				isEqual = false;
			} else if (!(((GARCHForecaster) obj).getAlphas().equals(this.getAlphas()))) {
				isEqual = false;
			} else if (!(((GARCHForecaster) obj).getBetas().equals(this.getBetas()))) {
				isEqual = false;
			} else if (!(((GARCHForecaster) obj).getOmega().equals(this.getOmega()))) {
				isEqual = false;
			}

		} else {
			isEqual = false;
		}

		return isEqual;
	}

	@Override
	public String toString() {
		String stringRepresentation = this.getClass().getSimpleName() + "[";
		stringRepresentation += "omega = " + this.omega + "; ";
		stringRepresentation += "alphas: ";

		Integer alphaIndex = 1;
		for (Double alpha : this.alphas) {
			stringRepresentation += "[alpha_" + alphaIndex.toString();
			alphaIndex++;
			stringRepresentation += "=" + alpha.toString() + "]";
		}

		Integer betaIndex = 1;
		stringRepresentation += ";betas:";
		for (Double beta : this.betas) {
			stringRepresentation += "[beta_" + betaIndex.toString();
			betaIndex++;
			stringRepresentation += "=" + beta.toString() + "]";
		}
		stringRepresentation += "]";

		return stringRepresentation;
	}

	@Override
	public Double forecast(final LinkedList<Double> lagResiduals, final LinkedList<Double> lagVariances) {
		// length condition
		if (lagResiduals.size() == this.q && lagVariances.size() == p) {

			// stating with omega.
			Double varianceNextPeriod = this.omega;

			// continue with lag residuals, weights are the alphas.
			@SuppressWarnings("unchecked")
			LinkedList<Double> residualsClone = (LinkedList<Double>) lagResiduals.clone();
			for (Iterator<Double> nextAlphaIterator = this.alphas.descendingIterator(); nextAlphaIterator.hasNext();) {
				Double lastResidual = residualsClone.poll();
				varianceNextPeriod += (nextAlphaIterator.next() * lastResidual * lastResidual);
			}

			// add lag variance, weights are the betas.
			@SuppressWarnings("unchecked")
			LinkedList<Double> variancesClone = (LinkedList<Double>) lagVariances.clone();
			for (Iterator<Double> nextBetaIterator = this.betas.descendingIterator(); nextBetaIterator.hasNext();) {
				varianceNextPeriod += (nextBetaIterator.next() * variancesClone.poll());
			}

			return varianceNextPeriod;
		} else {
			throw new IllegalArgumentException("Length of residuals and/or variances is not equal q/p.");
		}
	}

	@Override
	public Double forecast(final LinkedList<Double> lagResiduals, final LinkedList<Double> lagVariances,
			Integer timeHorizon) {
		// TODO time Horizon formula
		return this.forecast(lagResiduals, lagVariances);
	}

	@Override
	public Double forecast(LinkedList<Double> residuals, Integer timeHorizon) {
		// TODO time Horizon formula
		return this.forecast(residuals);
	}

	@Override
	public Double forecast(final LinkedList<Double> sampleResiduals) {

		if (sampleResiduals.size() >= this.q) {
			LinkedList<Double> lagVariances = new LinkedList<Double>();
			for (Iterator<Double> nextBetaIterator = this.betas.descendingIterator(); nextBetaIterator.hasNext();) {
				nextBetaIterator.next();

				// at first time, when no lag variances are known,
				// the variances are equal to r_0^2
				if (sampleResiduals.get(0) != null) {
					double firstResidual = sampleResiduals.get(0);
					lagVariances.add(firstResidual * firstResidual);
				} else {
					lagVariances.add(0.0);
				}
			}

			LinkedList<Double> lagResiduals = new LinkedList<Double>();
			for (int indexAlpha = 0; indexAlpha < this.alphas.size(); indexAlpha++) {
				lagResiduals.add(sampleResiduals.get(indexAlpha));
			}
			Double varianceNextPeriod = this.forecast(lagResiduals, lagVariances);

			// starting at alpha.size + 1 for recalculation
			for (int indexResiduals = this.alphas.size() + 1; indexResiduals < sampleResiduals
					.size(); indexResiduals++) {

				// update lagVariances
				lagVariances.removeFirst();
				lagVariances.addLast(varianceNextPeriod);

				lagResiduals.removeFirst();
				lagResiduals.addLast(sampleResiduals.get(indexResiduals));

				varianceNextPeriod = this.forecast(lagResiduals, lagVariances);
			}

			return varianceNextPeriod;
		} else {
			throw new IllegalArgumentException("Size of sampleResiduals have to be at least q (" + this.q
					+ "). Current Size: " + sampleResiduals.size());
		}

	}

	@Override
	public Integer getQ() {
		return this.q;
	}

	@Override
	public Integer getP() {
		return this.p;
	}

	public LinkedList<Double> getAlphas() {
		return alphas;
	}

	public LinkedList<Double> getBetas() {
		return betas;
	}

	public Double getOmega() {
		return omega;
	}

	@Override
	public IClone clone() {
		return this;
	}

}