package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class MultivariateGaussianMixtureModel implements
		MultivariateProbabilityDensityFunction {
	List<Pair<Double, MultivariateGaussianDensityFunction>> variables;

	@Override
	public double density(double[] x) {
		double result = 0.0;
		for (Pair<Double, MultivariateGaussianDensityFunction> variable : variables) {
			result += variable.getE1() * variable.getE2().density(x);
		}
		return result;
	}

	@Override
	public double cumulativeProbability(double[] x1, double[] x2) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void addVariable(MultivariateGaussianDensityFunction variable,
			double probability) {
		variables.add(new Pair<Double, MultivariateGaussianDensityFunction>(
				probability, variable));
	}

}
