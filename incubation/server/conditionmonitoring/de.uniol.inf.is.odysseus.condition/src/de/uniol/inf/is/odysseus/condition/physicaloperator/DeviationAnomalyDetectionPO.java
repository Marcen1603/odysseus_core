package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.enums.TrainingMode;
import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
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
	private Map<Long, List<T>> tupleMap;
	private boolean exactCalculation;

	public DeviationAnomalyDetectionPO(DeviationAnomalyDetectionAO ao, IGroupProcessor<T, T> groupProcessor) {
		this.interval = ao.getInterval();
		this.trainingMode = ao.getTrainingMode();
		this.valueAttributeName = ao.getNameOfValue();
		this.groupProcessor = groupProcessor;
		this.deviationInfo = new HashMap<Long, DeviationInformation>();
		this.exactCalculation = ao.getExactCalculation();

		if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			// Set the values for standardDeviation and mean manually
			this.manualMean = ao.getMean();
			this.manualStandardDeviation = ao.getStandardDeviation();
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			this.tuplesToLearn = ao.getTuplesToLearn();
		} else if (this.trainingMode.equals(TrainingMode.WINDOW)) {
			tupleMap = new HashMap<Long, List<T>>();
		}
	}

	@Override
	protected void process_open() throws OpenFailedException {
		groupProcessor.init();
	}

	@Override
	protected void process_next(T tuple, int port) {
		// Get the group for this tuple (e.g., the incoming values have
		// different contexts)
		Long gId = groupProcessor.getGroupID(tuple);
		DeviationInformation info = this.deviationInfo.get(gId);

		if (info == null) {
			info = new DeviationInformation();
			this.deviationInfo.put(gId, info);
		}

		double sensorValue = getValue(tuple);

		if (this.trainingMode.equals(TrainingMode.ONLINE)) {
			// Calculate new value for standard deviation
			info.standardDeviation = calcStandardDeviationOnline(sensorValue, info);
			processTuple(sensorValue, info.mean, info.standardDeviation, tuple);
		} else if (this.trainingMode.equals(TrainingMode.MANUAL)) {
			// Don't change the values - they are set by the user
			processTuple(sensorValue, manualMean, manualStandardDeviation, tuple);
		} else if (this.trainingMode.equals(TrainingMode.TUPLE_BASED)) {
			// Only change the values the first tuplesToLearn tuples (per group)
			if (info.n <= this.tuplesToLearn) {
				double sigma = calcStandardDeviationOnline(sensorValue, info);
				info.standardDeviation = sigma;
			}
			processTuple(sensorValue, info.mean, info.standardDeviation, tuple);
		} else if (this.trainingMode.equals(TrainingMode.WINDOW)) {
			// Use the window which is before this operator
			processTupleWithWindow(gId, tuple, info, this.exactCalculation);
		}
	}

	/**
	 * Processes a tuple if the operator is used in window mode. You have the
	 * choice to use the exact calculation or the (faster) approximate
	 * calculation.
	 * 
	 * @param gId
	 *            The id of the group for this tuple
	 * @param tuple
	 *            The new tuple
	 * @param info
	 *            The info object for this group
	 * @param exactCalculation
	 *            Your choice if you want to use exact calculation
	 */
	private void processTupleWithWindow(Long gId, T tuple, DeviationInformation info, boolean exactCalculation) {
		List<T> tuples = tupleMap.get(gId);
		if (tuples == null) {
			tuples = new ArrayList<T>();
			tupleMap.put(gId, tuples);
		}
		tuples.add(tuple);
		removeOldValues(tuples, tuple.getMetadata().getStart(), info, exactCalculation);
		double standardDeviation = 0;
		double mean = 0;
		if (exactCalculation) {
			addValue(getValue(tuple), info);
			standardDeviation = calcStandardDeviationApprox(info);
			mean = getMeanValue(info);
		} else {
			standardDeviation = calcStandardDeviationOffline(tuples, info);
			mean = info.mean;
		}

		processTuple(getValue(tuple), mean, standardDeviation, tuple);
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param tuples
	 *            The list of tuples from which the old ones have to be removed
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(List<T> tuples, PointInTime start, DeviationInformation info, boolean exactCalculation) {
		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			T next = iter.next();
			if (next.getMetadata().getEnd().beforeOrEquals(start)) {
				if (!exactCalculation)
					removeValue(getValue(next), info);
				iter.remove();
			}
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

	/*
	 * ----- ONLINE -----
	 */

	/**
	 * Calculates the standard deviation of the data seen so far (online).
	 * Algorithm like Knuth
	 * (http://en.wikipedia.org/wiki/Algorithms_for_calculating_variance#
	 * Online_algorithm)
	 * 
	 * @param newValue
	 *            The new value which came in
	 * @return The standard deviation of the data seen so far
	 */
	private double calcStandardDeviationOnline(double newValue, DeviationInformation info) {
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

	/*
	 * ----- WINDOW (approximate)------
	 */

	/**
	 * Adds a value to the approximate standard deviation (and mean) calculation
	 * 
	 * @param value
	 *            The value which has to be added
	 * @param info
	 *            The info object for this group
	 */
	private void addValue(double value, DeviationInformation info) {
		// It would be nice to know the mean of the distribution, but we don't
		// know before we see the tuples. So set k not to the mean, but
		// to the first value we see. A value near the mean would be perfect.
		// A problem occurs when the mean value changes dramatically.
		if (info.n == 0)
			info.k = value;

		info.n++;
		info.ex += value - info.k;
		info.ex2 += (value - info.k) * (value - info.k);
	}

	/**
	 * Removes a value from the approximate standard deviation (and mean)
	 * calculation
	 * 
	 * @param value
	 *            The value which has to be removed
	 * @param info
	 *            The info object for this group
	 */
	private void removeValue(double value, DeviationInformation info) {
		info.n--;
		info.ex -= value - info.k;
		info.ex2 -= (value - info.k) * (value - info.k);
	}

	/**
	 * Calculates the approximate mean value for the given group info object
	 * 
	 * @param info
	 *            The info object for this group
	 * @return The approximate mean value
	 */
	private double getMeanValue(DeviationInformation info) {
		return info.k + (info.ex / info.n);
	}

	/**
	 * Calculates an approximation of the standard calculation
	 * 
	 * @param info
	 *            The info object for this group
	 * @return The approximate standard deviation
	 */
	private double calcStandardDeviationApprox(DeviationInformation info) {
		double variance = (info.ex2 - ((info.ex + info.ex) / info.n)) / (info.n - 1);
		double standardDeviation = Math.sqrt(variance);
		return standardDeviation;
	}

	/*
	 * ------ OFFLINE -------
	 */

	/**
	 * Exact (but expensive) calculation of the standard deviation
	 * 
	 * @param tuples
	 *            List of tuples for which the standard deviation needs to be
	 *            calculated
	 * @param info
	 *            The info object for this group (there you can find the mean
	 *            after the calculation)
	 * @return The standard deviation of the given list
	 */
	private double calcStandardDeviationOffline(List<T> tuples, DeviationInformation info) {
		info.n = 0;
		info.sum1 = 0;
		info.sum2 = 0;

		for (T tuple : tuples) {
			info.n++;
			info.sum1 += getValue(tuple);
		}

		info.mean = info.sum1 / info.n;

		for (T tuple : tuples) {
			info.sum2 += (getValue(tuple) - info.mean) * (getValue(tuple) - info.mean);
		}

		double variance = info.sum2 / (info.n - 1);
		info.standardDeviation = Math.sqrt(variance);
		return info.standardDeviation;
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

	/**
	 * Helper function to easily get the value in the attribute specified in the
	 * valueAttributeName variable via the AO
	 * 
	 * @param tuple
	 *            The tuple where this attribute is in
	 * @return The double value in the tuple
	 */
	private double getValue(T tuple) {
		int valueIndex = getOutputSchema().findAttributeIndex(valueAttributeName);
		double sensorValue = tuple.getAttribute(valueIndex);
		return sensorValue;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	/**
	 * A small helper object to store the deviation information for the
	 * different groups
	 */
	class DeviationInformation {
		// For online calculation
		public long n;
		public double mean;
		public double m2;
		public double standardDeviation;

		// For window (approximate) calculation
		public double k;
		public double ex;
		public double ex2;

		// For offline calculation
		public double sum1;
		public double sum2;
	}

}
