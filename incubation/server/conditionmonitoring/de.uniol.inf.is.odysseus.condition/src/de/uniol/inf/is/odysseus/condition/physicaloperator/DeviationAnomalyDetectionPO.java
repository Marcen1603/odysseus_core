package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * This operator finds anomalies / outliers. Therefore, the mean gaussian
 * standard deviation is calculated and compared to the newest tuple
 * 
 * @author Tobias Brandt
 *
 */
@SuppressWarnings("rawtypes")
public class DeviationAnomalyDetectionPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private String valueAttributeName;

	private double interval;
	private TrainingMode trainingMode;
	private double manualStandardDeviation;
	private double manualMean;
	private long tuplesToLearn;
	private IGroupProcessor<T, T> groupProcessor;
	private Map<Long, DeviationInformation> deviationInfo;

	public DeviationAnomalyDetectionPO() {
		super();
		this.interval = 3.0;
		this.trainingMode = TrainingMode.ONLINE;
		this.valueAttributeName = "value";
		this.deviationInfo = new HashMap<Long, DeviationInformation>();
	}

	public DeviationAnomalyDetectionPO(DeviationAnomalyDetectionAO ao, IGroupProcessor<T, T> groupProcessor) {
		this.interval = ao.getInterval();
		this.trainingMode = ao.getTrainingMode();
		this.valueAttributeName = ao.getNameOfValue();
		this.groupProcessor = groupProcessor;
		this.deviationInfo = new HashMap<Long, DeviationInformation>();

		if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			// Set the values for standardDeviation and mean manually
			this.manualMean = ao.getMean();
			this.manualStandardDeviation = ao.getStandardDeviation();
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			this.tuplesToLearn = ao.getTuplesToLearn();
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		groupProcessor.init();
	}

	@Override
	protected void process_next(T tuple, int port) {
		Long gId = groupProcessor.getGroupID(tuple);
		DeviationInformation info = this.deviationInfo.get(gId);

		if (info == null) {
			info = new DeviationInformation();
			this.deviationInfo.put(gId, info);
		}

		int valueIndex = getOutputSchema().findAttributeIndex(valueAttributeName);
		double sensorValue = tuple.getAttribute(valueIndex);

		if (this.trainingMode.equals(TrainingMode.ONLINE)) {
			// Calculate new value for standard deviation
			info.standardDeviation = calcStandardDeviation(sensorValue, info);
			processTuple(sensorValue, info.mean, info.standardDeviation, tuple);
		} else if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			// Don't change the values - they are set by the user
			processTuple(sensorValue, manualMean, manualStandardDeviation, tuple);
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			// Only change the values the first tuplesToLearn tuples (per group)
			if (info.n <= this.tuplesToLearn) {
				double sigma = calcStandardDeviation(sensorValue, info);
				info.standardDeviation = sigma;
			}
			processTuple(sensorValue, info.mean, info.standardDeviation, tuple);
		}
	}

	/**
	 * Processes a tuple with the given values. This includes the calculation of
	 * the anomaly score. Transfers the tuple if it is an anomaly
	 * 
	 * @param sensorValue
	 *            The value of the sensor
	 * @param mean
	 *            The mean for that group
	 * @param standardDeviation
	 *            The standard deviation for that group
	 * @param tuple
	 *            The tuple which has to be transfered if it is an anomaly
	 */
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
	private double calcStandardDeviation(double newValue, DeviationInformation info) {
		info.n += 1;
		double delta = newValue - info.mean;
		info.mean += (delta / info.n);
		info.m2 += delta * (newValue - info.mean);

		if (info.n < 2)
			return 0;

		double variance = info.m2 / (info.n - 1);
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

	class DeviationInformation {
		public long n;
		public double mean;
		public double m2;
		public double standardDeviation;
	}

}
