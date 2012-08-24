package de.uniol.inf.is.odysseus.probabilistic.math;

public class GenericDensityFunction implements ProbabilityDensityFunction {
    private CumulativeDistributionFunction cdf;

    @Override
    public double density(final double x) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double cumulativeProbability(final double x1, final double x2) {
        return this.cdf.evaluate(x2) - this.cdf.evaluate(x1);
    }

    public void setCumulativeDistributionFunction(final CumulativeDistributionFunction cdf) {
        this.cdf = cdf;
    }

}
