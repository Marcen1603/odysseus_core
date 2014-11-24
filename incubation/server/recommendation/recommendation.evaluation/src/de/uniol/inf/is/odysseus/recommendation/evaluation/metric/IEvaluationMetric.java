package de.uniol.inf.is.odysseus.recommendation.evaluation.metric;

public interface IEvaluationMetric {

	public Object calculateMetric(Object classValue, Double clazzValue, double fadingFactor);
	
}
