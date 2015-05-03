package de.uniol.inf.is.odysseus.condition.physicaloperator;

import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

@SuppressWarnings("rawtypes")
public class DeviationAnomalyDetectionPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> {

	private String valueAttributeName;
	
	private long n = 0;
	private double mean = 0;
	private double m2 = 0;

	private double interval;
	private TrainingMode trainingMode;
	private double manualStandardDeviation;
	private long tuplesToLearn;

	public DeviationAnomalyDetectionPO() {
		super();
		this.interval = 3.0;
		this.trainingMode = TrainingMode.ONLINE;
		this.valueAttributeName = "value";
	}

	public DeviationAnomalyDetectionPO(DeviationAnomalyDetectionAO ao) {
		this.interval = ao.getInterval();
		this.trainingMode = ao.getTrainingMode();
		this.valueAttributeName = ao.getNameOfValue();
		
		if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			// Set the values for standardDeviation and mean manually
			this.mean = ao.getMean();
			this.manualStandardDeviation = ao.getStandardDeviation();
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			this.tuplesToLearn = ao.getTuplesToLearn();
		}
	}

	@Override
	protected void process_next(T tuple, int port) {

		int valueIndex = getOutputSchema().findAttributeIndex(valueAttributeName);
		double sensorValue = tuple.getAttribute(valueIndex);

		if (this.trainingMode.equals(TrainingMode.ONLINE)) {
			// Calculate new value for standard deviation
			double sigma = calcStandardDeviation(sensorValue);
			processTuple(sensorValue, mean, sigma, tuple);
		} else if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			processTuple(sensorValue, mean, manualStandardDeviation, tuple);
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			if (n <= this.tuplesToLearn) {
				double sigma = calcStandardDeviation(sensorValue);
				this.manualStandardDeviation = sigma;
			}
			processTuple(sensorValue, mean, manualStandardDeviation, tuple);
		}
	}

	private void processTuple(double sensorValue, double mean, double standardDeviation, Tuple tuple) {
		if (sensorValue < mean - interval * standardDeviation || sensorValue > mean + interval * standardDeviation) {
			// Maybe here we have an anomaly
			double anomalyScore = calcAnomalyScore(sensorValue, mean, standardDeviation, interval);
			Tuple newTuple = tuple.append(anomalyScore);
			newTuple = newTuple.append(mean);
			newTuple = newTuple.append(standardDeviation);
			transfer(newTuple);
			return;
		}
	}

	/**
	 * Calculates the standard deviation of the data seen so far. Algorithm like
	 * Knuth (http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#
	 * Online_algorithm)
	 * 
	 * @param newValue
	 *            The new value which came in
	 * @return The standard deviation of the data seen so far
	 */
	private double calcStandardDeviation(double newValue) {
		n += 1;
		double delta = newValue - mean;
		mean += (delta / n);
		m2 += delta * (newValue - mean);

		if (n < 2)
			return 0;

		double variance = m2 / (n - 1);
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}

	/**
	 * Calculates the anomaly score, a value between 0 and 1. (Will actually
	 * never reach 1)
	 * 
	 * @param distance
	 *            Distance to good area
	 * @return Anomaly score (0, 1]
	 */
	private double calcAnomalyScore(double sensorValue, double mean, double standardDeviation, double interval) {

		double minValue = mean - (interval * standardDeviation);
		double maxValue = mean + (interval * standardDeviation);

		double distance = sensorValue > maxValue ? sensorValue - maxValue : sensorValue < minValue ? minValue
				- sensorValue : 0;

		double div = distance / (maxValue - minValue);

		double addValue = 0.5;
		double anomalyScore = 0;
		while (div > 0) {
			if (div >= 1) {
				anomalyScore += addValue;
				addValue /= 2;
				div -= 1;
			} else {
				anomalyScore += div * addValue;
				div -= 1;
			}
		}

		return anomalyScore;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
