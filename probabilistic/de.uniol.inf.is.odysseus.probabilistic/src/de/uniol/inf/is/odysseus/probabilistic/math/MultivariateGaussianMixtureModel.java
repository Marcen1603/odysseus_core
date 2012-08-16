package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class MultivariateGaussianMixtureModel implements MultivariateProbabilityDensityFunction {
    List<Pair<Double, MultivariateGaussianDensityFunction>> variables;

    @Override
    public double density(final double[] x) {
        double result = 0.0;
        for (final Pair<Double, MultivariateGaussianDensityFunction> variable : this.variables) {
            result += variable.getE1() * variable.getE2().density(x);
        }
        return result;
    }

    @Override
    public double cumulativeProbability(final double[] x1, final double[] x2) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void addVariable(final MultivariateGaussianDensityFunction variable, final double probability) {
        this.variables.add(new Pair<Double, MultivariateGaussianDensityFunction>(probability, variable));
    }

}
