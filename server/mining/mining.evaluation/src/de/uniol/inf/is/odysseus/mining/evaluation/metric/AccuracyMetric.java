package de.uniol.inf.is.odysseus.mining.evaluation.metric;

import java.util.List;

public class AccuracyMetric extends ClassificationMetric {

	private double hits = 0.0;
	private double n = 0.0;
	
	public AccuracyMetric(List<String> nominals, boolean inverse) {
		super(nominals, inverse);
		hits = 0.0;
		n = 0.0;
	}
	
	public AccuracyMetric(List<String> nominals) {
		this(nominals, false);
	}
	
	@Override
	public Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		//interpret clazzValue as index of class-nominals
		String clazz = nominals.get(clazzValue.intValue());
		hits = hits * fadingFactor + (clazz.equals(classValue) ? 1 - loss : loss);
		n = n * fadingFactor + 1.0;
		return (hits / n) ;
	}
	
}
