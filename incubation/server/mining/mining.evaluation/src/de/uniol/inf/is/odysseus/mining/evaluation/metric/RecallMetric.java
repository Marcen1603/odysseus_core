package de.uniol.inf.is.odysseus.mining.evaluation.metric;

import java.util.Arrays;
import java.util.List;

public class RecallMetric extends ClassificationMetric {

	private double[] classHits;
	private double[] n;
	
	public RecallMetric(List<String> nominals, boolean inverse) {
		super(nominals, inverse);
		classHits = new double[nominals.size()];
		n = new double[nominals.size()];
		
		Arrays.fill(classHits, 0.0);
		Arrays.fill(n, 0.0);
	}
	
	public RecallMetric(List<String> nominals) {
		this(nominals, false);
	}

	@Override
	public Double calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		//interpret clazzValue as index of class-nominals
		String clazz = nominals.get(clazzValue.intValue());
		//manipulate hits- and n-values for the true class
		int classIndex = nominals.indexOf(classValue);
		classHits[classIndex] = classHits[classIndex] * fadingFactor + (clazz.equals(classValue) ? 1 - loss : loss);
		n[classIndex] = n[classIndex] * fadingFactor + 1.0;
		
		double nSum = 0.0;
		double classHitsSum = 0.0;
		//sum up hits- and n-values
		for (int i = 0; i < classHits.length; i++) {
			classHitsSum += classHits[i];
			nSum += n[i];
		}
		
		if(nSum == 0.0){
			return 0.0;
		}
		return classHitsSum / nSum ;
	}

}
