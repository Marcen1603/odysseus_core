package de.uniol.inf.is.odysseus.mining.evaluation.metric;

public class RAEMetric extends RegressionMetric {

	double deviationMean;
	double mean;
	
	public RAEMetric() {
		this.deviationMean = 0.0;
		this.mean = 0.0;
	}
	
	@Override
	public Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		deviation = deviation * fadingFactor + Math.abs(clazzValue - ((Number) classValue).doubleValue());
		mean += ((Number) classValue).doubleValue();
		deviationMean = deviationMean * fadingFactor + Math.abs((mean/++n) - ((Number) classValue).doubleValue());
		return deviation / deviationMean;
	}

}
