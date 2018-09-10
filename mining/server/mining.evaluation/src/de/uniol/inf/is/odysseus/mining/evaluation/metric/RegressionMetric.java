package de.uniol.inf.is.odysseus.mining.evaluation.metric;

public abstract class RegressionMetric implements IEvaluationMetric {
	
	double deviation;
	double n;
	
	/**
	 * A metric for evaluating a regressor.
	 */
	public RegressionMetric() {
		this.deviation = 0.0;
		this.n = 0.0;
	}
	
	@Override
	public abstract Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor);

}
