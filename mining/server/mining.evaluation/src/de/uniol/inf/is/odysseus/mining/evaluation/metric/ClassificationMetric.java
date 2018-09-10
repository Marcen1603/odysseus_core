package de.uniol.inf.is.odysseus.mining.evaluation.metric;

import java.util.List;

public abstract class ClassificationMetric implements IEvaluationMetric {

	/**
	 * the list of class-nominals used to determine whether a classification was correct.
	 */
	protected List<String> nominals;
	
	/**
	 * specifies the loss function of the metric.
	 * e.g. the error loss funtion is either 0 if the classification was correct or 1 otherwise.
	 */
	protected int loss;
	
	/**
	 * A metric for evaluating a classificator.
	 * @param nominals the list of class-nominals used to determine whether a classification was correct.
	 * @param inverse specifies if the metric-value should be inversed (e.g. error is 1 - accuracy).
	 */
	public ClassificationMetric(List<String> nominals, boolean inverse) {
		this.nominals = nominals;
		this.loss = inverse ? 1 : 0;
	}
	
	@Override
	public abstract Object calculateMetric(Object classValue, Double clazzValue, double fadingFactor);
}
