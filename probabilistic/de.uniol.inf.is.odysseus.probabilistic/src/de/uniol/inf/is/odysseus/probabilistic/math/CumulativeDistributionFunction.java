package de.uniol.inf.is.odysseus.probabilistic.math;

public interface CumulativeDistributionFunction {
    CumulativeDistributionFunction add();

    double evaluate(double x);
}
