package de.uniol.inf.is.odysseus.mining.evaluation.metric;

import java.util.Arrays;
import java.util.List;

public class PrecisionMetric extends ClassificationMetric {

	private double[] classHits;
	private double[] n;
	
	public PrecisionMetric(List<String> nominals, boolean inverse) {
		super(nominals, inverse);
		classHits = new double[nominals.size()];
		n = new double[nominals.size()];
		
		Arrays.fill(classHits, 0.0);
		Arrays.fill(n, 0.0);
	}
	
	public PrecisionMetric(List<String> nominals) {
		this(nominals, false);
	}

	@Override
	public String calculateMetric(Object classValue, Double clazzValue, double fadingFactor) {
		//interpret clazzValue as index of class-nominals
		String clazz = nominals.get(clazzValue.intValue());
		//manipulate hits- and n-values for the predicted class
		classHits[clazzValue.intValue()] = classHits[clazzValue.intValue()] * fadingFactor + (clazz.equals(classValue) ? 1 - loss : loss);
		n[clazzValue.intValue()] = n[clazzValue.intValue()] * fadingFactor + 1.0;

		//better: use separate attributes for class-precision values
		String returnString = "";
		for (int i = 0; i < classHits.length; i++) {
			if(n[i] == 0) {
				continue;
			}
			returnString += "\"" + nominals.get(i) + "\":" + String.format("%.3f", (classHits[i] / n[i])) + "; ";
		}
		return returnString;
		
		//the weigthed average is equal to accuracy of model
//		double nSum = 0.0;
//		double classHitsSum = 0.0;
//		//sum up hits- and n-values
//		for (int i = 0; i < classHits.length; i++) {
//			classHitsSum += classHits[i];
//			nSum += n[i];
//		}
//
//		if(nSum == 0.0){
//			return 0.0;
//		}
//		return classHitsSum / nSum ;
	}
}
