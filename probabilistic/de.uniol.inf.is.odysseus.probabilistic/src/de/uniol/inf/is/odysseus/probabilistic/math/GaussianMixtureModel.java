package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class GaussianMixtureModel implements ProbabilityDensityFunction {

    List<Pair<Double, GaussianDensityFunction>> variables;

    @Override
    public double density(final double x) {
        double result = 0.0;
        for (final Pair<Double, GaussianDensityFunction> variable : this.variables) {
            result += variable.getE1() * variable.getE2().density(x);
        }
        return result;
    }

    @Override
    public double cumulativeProbability(final double x1, final double x2) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void addVariable(final GaussianDensityFunction variable, final double probability) {
        this.variables.add(new Pair<Double, GaussianDensityFunction>(probability, variable));
    }

}
