package de.uniol.inf.is.odysseus.mining.evaluation.metric;

public class MSEMetric extends RegressionMetric {

	@Override
	public Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		deviation = deviation * fadingFactor + Math.pow(clazzValue - ((Number) classValue).doubleValue(), 2.0);
		n = n * fadingFactor + 1.0;
		return deviation / n;
	}

}
