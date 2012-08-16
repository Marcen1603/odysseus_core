package de.uniol.inf.is.odysseus.probabilistic.math;

import org.apache.commons.math3.distribution.NormalDistribution;

public class GaussianDensityFunction implements ProbabilityDensityFunction {

    private final NormalDistribution distribution;

    public GaussianDensityFunction(final double mean, final double standardDeviation) {
        this.distribution = new NormalDistribution(mean, standardDeviation);
    }

    @Override
    public double density(final double x) {
        return this.distribution.density(x);
    }

    @Override
    public double cumulativeProbability(final double x1, final double x2) {
        return this.distribution.cumulativeProbability(x1, x2);
    }

    public GaussianDensityFunction add(final GaussianDensityFunction probabilityDensityFunction) {
        return new GaussianDensityFunction(this.distribution.getMean()
                + probabilityDensityFunction.distribution.getMean(), this.distribution.getStandardDeviation()
                + probabilityDensityFunction.distribution.getStandardDeviation());
    }

    public GaussianDensityFunction substract(final GaussianDensityFunction probabilityDensityFunction) {
        return new GaussianDensityFunction(this.distribution.getMean()
                - probabilityDensityFunction.distribution.getMean(), this.distribution.getStandardDeviation()
                - probabilityDensityFunction.distribution.getStandardDeviation());
    }

    @Override
    public GaussianDensityFunction clone() {
        return new GaussianDensityFunction(this.distribution.getMean(), this.distribution.getStandardDeviation());
    }
}
