package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class GaussianMixtureModel implements ProbabilityDensityFunction {

    List<Pair<ProbabilityDensityFunction, Double>> variables;

    @Override
    public double density(final double x) {
        double result = 0.0;
        for (final Pair<ProbabilityDensityFunction, Double> variable : this.variables) {
            result += variable.getE2() * variable.getE1().density(x);
        }
        return result;
    }

    @Override
    public double cumulativeProbability(final double x1, final double x2) {
        double result = 0.0;
        for (final Pair<ProbabilityDensityFunction, Double> variable : this.variables) {
            result += variable.getE2() * variable.getE1().cumulativeProbability(x1, x2);
        }
        return result;
    }

    public void addVariable(final ProbabilityDensityFunction variable, final double probability) {
        this.variables.add(new Pair<ProbabilityDensityFunction, Double>(variable, probability));
    }

}
