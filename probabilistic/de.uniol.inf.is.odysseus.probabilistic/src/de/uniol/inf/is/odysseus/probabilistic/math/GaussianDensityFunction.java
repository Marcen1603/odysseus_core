package de.uniol.inf.is.odysseus.probabilistic.math;

import org.apache.commons.math3.distribution.NormalDistribution;

public class GaussianDensityFunction implements ProbabilityDensityFunction {

	private final NormalDistribution distribution;

	public GaussianDensityFunction(double mean, double standardDeviation) {
		this.distribution = new NormalDistribution(mean, standardDeviation);
	}

	public double density(double x) {
		return distribution.density(x);
	}

	@Override
	public double cumulativeProbability(double x1, double x2) {
		return this.distribution.cumulativeProbability(x1, x2);
	}

	public GaussianDensityFunction add(
			GaussianDensityFunction probabilityDensityFunction) {
		return new GaussianDensityFunction(this.distribution.getMean()
				+ probabilityDensityFunction.distribution.getMean(),
				this.distribution.getStandardDeviation()
						+ probabilityDensityFunction.distribution
								.getStandardDeviation());
	}

	public GaussianDensityFunction substract(
			GaussianDensityFunction probabilityDensityFunction) {
		return new GaussianDensityFunction(this.distribution.getMean()
				- probabilityDensityFunction.distribution.getMean(),
				this.distribution.getStandardDeviation()
						- probabilityDensityFunction.distribution
								.getStandardDeviation());
	}

	@Override
	public GaussianDensityFunction clone() {
		return new GaussianDensityFunction(this.distribution.getMean(),
				this.distribution.getStandardDeviation());
	}
}
