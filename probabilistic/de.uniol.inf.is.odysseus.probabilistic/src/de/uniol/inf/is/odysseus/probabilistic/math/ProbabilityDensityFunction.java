package de.uniol.inf.is.odysseus.probabilistic.math;

public interface ProbabilityDensityFunction {

	double density(double x);

	double cumulativeProbability(double x1, double x2);
}
