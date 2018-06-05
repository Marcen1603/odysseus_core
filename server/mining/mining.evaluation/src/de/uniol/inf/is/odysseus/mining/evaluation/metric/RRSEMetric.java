package de.uniol.inf.is.odysseus.mining.evaluation.metric;

public class RRSEMetric extends RegressionMetric {

	double deviationMean;
	double mean;
	
	public RRSEMetric() {
		this.deviationMean = 0.0;
		this.mean = 0.0;
	}
	
	@Override
	public Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		deviation = deviation * fadingFactor + Math.pow(clazzValue - ((Number) classValue).doubleValue(), 2.0);
		mean += ((Number) classValue).doubleValue();
		deviationMean = deviationMean * fadingFactor + Math.pow((mean/++n) - ((Number) classValue).doubleValue(), 2.0);
		return Math.sqrt(deviation / deviationMean);
	}

}
