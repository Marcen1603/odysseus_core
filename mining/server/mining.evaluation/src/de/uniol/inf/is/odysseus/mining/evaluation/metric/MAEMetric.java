package de.uniol.inf.is.odysseus.mining.evaluation.metric;

public class MAEMetric extends RegressionMetric {
	
	@Override
	public Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		deviation = deviation * fadingFactor + Math.abs(clazzValue - ((Number) classValue).doubleValue());
		n = n * fadingFactor + 1.0;
		return deviation / n;
	}

}
