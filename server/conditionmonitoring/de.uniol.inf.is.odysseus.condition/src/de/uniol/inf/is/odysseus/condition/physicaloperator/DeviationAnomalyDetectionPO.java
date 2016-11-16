package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.condition.logicaloperator.DeviationAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.Heartbeat;
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

	public static final int DATA_PORT = 0;
	public static final int LEARN_PORT = 1;

	private String valueAttributeName;

	private double interval;
	private IGroupProcessor<T, T> groupProcessor;
	// groupId, PointInTime from when it's active and information
	private Map<Object, Map<PointInTime, DeviationInformation>> deviationInfo;
	// For window checking
	private Map<Object, List<T>> tupleMap;
	// For tuples where the info is too old
	private List<T> futureTupleList;
	private boolean exactCalculation;

	private boolean windowChecking;
	private Map<Object, Boolean> lastWindowWithAnomaly;
	private boolean onlyOnStart;
	private Map<Object, Boolean> sendEnd;

	// For groups that did not occur before
	private boolean deliverUnlearnedTuples;

	// Parameters to avoid early false-positives
	private int tuplesToWait;
	private double maxRelativeChange;
	private Map<Object, Boolean> startToDeliver;
	private Map<Object, Boolean> hadLittleChange;

	// If true, sends a tuple with anomaly score = 0, if the last window had an
	// anomaly, but this one didn't
	private boolean reportEndOfAnomalyWindows;
	private Map<Object, T> lastAnomalies;

	private String standardDeviationAttributeName = "standardDeviation";
	private String meanAttributeName = "mean";

	// timeSensitive for punctuations (for interval analysis)
	private Map<Object, T> lastDataTuples;
	private boolean isTimeSensitive;

	// The last time we send a tuples based on a punctuation
	private PointInTime lastTimePunctuationBasedTuple;

	public DeviationAnomalyDetectionPO(DeviationAnomalyDetectionAO ao, IGroupProcessor<T, T> groupProcessor) {
		this.futureTupleList = new ArrayList<>();

		this.interval = ao.getInterval();
		this.groupProcessor = groupProcessor;
		this.valueAttributeName = ao.getNameOfValue();
		this.deviationInfo = new HashMap<>();

		this.windowChecking = ao.isWindowChecking();
		this.tupleMap = new HashMap<>();

		this.lastWindowWithAnomaly = new HashMap<>();

		this.reportEndOfAnomalyWindows = ao.isReportEndOfAnomalyWindows();
		this.lastAnomalies = new HashMap<Object, T>();
		this.deliverUnlearnedTuples = ao.isDeliverUnlearnedTuples();

		this.tuplesToWait = ao.getTuplesToWait();
		this.maxRelativeChange = ao.getMaxRelativeChange();
		this.startToDeliver = new HashMap<>();
		this.hadLittleChange = new HashMap<>();

		this.isTimeSensitive = ao.isTimeSensitive();
		this.lastDataTuples = new HashMap<>();

		this.onlyOnStart = ao.isOnlyOnChange();
		this.sendEnd = new HashMap<>();
	}

	@Override
	protected void process_open() throws OpenFailedException {
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	@Override
	protected void process_next(T tuple, int port) {

		// Get the group for this tuple (e.g., the incoming values have
		// different contexts)
		Object gId = groupProcessor.getGroupID(tuple);
		Map<PointInTime, DeviationInformation> infoMap = this.deviationInfo.get(gId);

		if (infoMap == null) {
			// First occurrence of this group
			infoMap = new HashMap<PointInTime, DeviationInformation>();
			this.deviationInfo.put(gId, infoMap);
			this.sendEnd.put(gId, true);
			this.startToDeliver.put(gId, false);
			this.hadLittleChange.put(gId, false);
			this.lastWindowWithAnomaly.put(gId, false);
		}

		// Get correct information
		DeviationInformation info = getInfoForTuple(tuple);

		if (info != null
				&& (!(this.maxRelativeChange > 0) || (this.maxRelativeChange > 0 && hadLittleChange.get(gId)))) {
			if (info.counter > this.tuplesToWait) {
				this.startToDeliver.put(gId, true);
			}
		}

		if (port == DATA_PORT && startToDeliver.get(gId)) {
			// Process data
			lastDataTuples.put(gId, tuple);
			if (info != null) {
				double sensorValue = getValue(tuple);
				processTuple(gId, sensorValue, tuple, info);
			} else {
				// We don't have an info for this tuple, therefore save it for
				// later
				futureTupleList.add(tuple);
			}
		} else if (port == DATA_PORT && deliverUnlearnedTuples) {
			// We don't have an anomaly as we don't know the deviation. But the
			// user wants to get such tuples, so there it is.
			transferTuple(tuple, 0.0, 0.0, getValue(tuple), 0.0);
		} else if (port == LEARN_PORT) {
			DeviationInformation oldInfo = getNewestInfoForTuple(tuple);
			// We always need a new object as the metadata (starttimestamp)
			// changes every time
			DeviationInformation newInformation = new DeviationInformation();
			infoMap.put(tuple.getMetadata().getStart(), newInformation);

			// Old information - if is exists
			double oldMean = 0;
			if (oldInfo != null) {
				oldMean = oldInfo.mean;

				// Count, how many updates this information got
				newInformation.counter = oldInfo.counter + 1;
			}

			// Update deviation information
			int stdDevIndex = getInputSchema(LEARN_PORT).findAttributeIndex(standardDeviationAttributeName);
			if (stdDevIndex >= 0) {
				newInformation.standardDeviation = tuple.getAttribute(stdDevIndex);
			}

			int meanIndex = getInputSchema(LEARN_PORT).findAttributeIndex(meanAttributeName);
			if (meanIndex >= 0) {
				newInformation.mean = tuple.getAttribute(meanIndex);
			}
			double newMean = newInformation.mean;
			if (this.maxRelativeChange > 0 && oldMean != 0
					&& (Math.abs(oldMean - newMean) / oldMean) < this.maxRelativeChange) {
				this.hadLittleChange.put(gId, true);
			}

			// With this new knowledge maybe we can process a few tuples we
			// weren't able to before
			List<T> toRemove = new ArrayList<>();
			for (T oldTuple : futureTupleList) {
				// Get correct information
				DeviationInformation correctInfo = getInfoForTuple(oldTuple);
				if (correctInfo != null) {
					double sensorValue = getValue(oldTuple);
					processTuple(gId, sensorValue, oldTuple, correctInfo);
					toRemove.add(oldTuple);
				}
			}
			futureTupleList.removeAll(toRemove);
		}
	}

	/**
	 * Searches for the correct information for this tuple. The correct
	 * information is the one with the newest timestamp that is older than the
	 * one of the tuple.
	 *
	 * The new info and the next tuple normally have the same timestamp. We
	 * don't use the info which is made with the current tuple cause we want an
	 * info which is uninfluenced by a potential anomaly. Therefore, is searches
	 * for "startTime.before(tupleStartTime)" and not
	 * "startTime.beforeOrEquals(tupleStartTime)"
	 *
	 * Also purges old tuples in the infoMap
	 *
	 * @param tuple
	 * @return
	 */
	private DeviationInformation getInfoForTuple(T tuple) {
		Object gId = groupProcessor.getGroupID(tuple);
		Map<PointInTime, DeviationInformation> infoMap = this.deviationInfo.get(gId);
		if (tuple == null) {
			return null;
		}
		PointInTime tupleStartTime = tuple.getMetadata().getStart();

		// Search for the newest information which fits
		PointInTime bestPointInTime = null;
		PointInTime biggerThanBestPoint = null;
		for (PointInTime startTime : infoMap.keySet()) {
			if ((bestPointInTime == null || startTime.after(bestPointInTime)) && startTime.before(tupleStartTime)) {
				bestPointInTime = startTime;
			}
		}

		for (PointInTime startTime : infoMap.keySet()) {
			// We need to know if there is a tuple after the best tuple. Cause
			// if not, maybe there will be a better fitting info in the future
			// and then we would need to wait
			if (bestPointInTime != null && startTime.after(bestPointInTime)) {
				biggerThanBestPoint = startTime;
			}
		}

		if (bestPointInTime != null && biggerThanBestPoint != null && biggerThanBestPoint.after(bestPointInTime)) {
			// Purge elements before
			List<PointInTime> toRemove = new ArrayList<>();
			for (PointInTime startTime : infoMap.keySet()) {
				if (startTime.before(bestPointInTime)) {
					toRemove.add(startTime);
				}
			}
			for (PointInTime startTime : toRemove) {
				infoMap.remove(startTime);
			}
			return infoMap.get(bestPointInTime);
		}
		return null;
	}

	/**
	 * Returns the newest info which is older than the given tuple and does not
	 * check, if there is a tuple after that one.
	 *
	 * @param tuple
	 * @return
	 */
	private DeviationInformation getNewestInfoForTuple(T tuple) {
		Object gId = groupProcessor.getGroupID(tuple);
		Map<PointInTime, DeviationInformation> infoMap = this.deviationInfo.get(gId);
		PointInTime tupleStartTime = tuple.getMetadata().getStart();

		// Search for the newest information which fits
		PointInTime bestPointInTime = null;
		for (PointInTime startTime : infoMap.keySet()) {
			if ((bestPointInTime == null || startTime.after(bestPointInTime)) && startTime.before(tupleStartTime)) {
				bestPointInTime = startTime;
			}
		}

		if (bestPointInTime != null) {
			return infoMap.get(bestPointInTime);
		}
		return null;
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
	private void removeOldValues(List<T> tuples, PointInTime start, DeviationInformation info, boolean exactCalculation,
			Object gId) {
		boolean skippedSomething = false;
		boolean needToSendEndOfAnomalies = this.lastWindowWithAnomaly.get(gId);

		T lastAnomaly = lastAnomalies.get(gId);

		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			T next = iter.next();
			if (next.getMetadata().getEnd().beforeOrEquals(start)) {
				if (lastAnomaly != null && next.getMetadata().getStart().before(lastAnomaly.getMetadata().getStart())) {
					// These are the tuples which were in the same window before
					// the first anomaly occurred
					iter.remove();
				} else if (this.windowChecking && this.lastWindowWithAnomaly.get(gId)
						&& next.getMetadata().getStart().beforeOrEquals(lastAnomaly.getMetadata().getEnd())) {
					// This tuple belongs to a window which had an anomaly. But
					// only send this further, if the next window has an
					// anomaly, too. Hence, do not delete them now.
					skippedSomething = true;
				} else {
					this.lastWindowWithAnomaly.put(gId, false);
					if (skippedSomething) {
						// We have to start from the beginning cause the
						// condition for the first if statement changed
						iter = tuples.iterator();
						skippedSomething = false;
						continue;
					} else if (needToSendEndOfAnomalies) {
						// This is the first tuple which was no anomaly and
						// there won't be anomalies in the next window, hence we
						// have to report the end of the anomalies
						needToSendEndOfAnomalies = false;
						this.sendEnd.put(gId, true);
						if (this.reportEndOfAnomalyWindows) {
							transferTuple(next, 0.0, 0.0, info.mean, info.standardDeviation);
						}
					}
					// If we will remove the tuple from the window, we can send
					// a heartbeat, that we are at least at this timestamp
					Heartbeat beat = Heartbeat.createNewHeartbeat(next.getMetadata().getStart());
					sendPunctuation(beat);
					iter.remove();
				}
			}
		}
	}

	/**
	 * Processes a tuple with the given values. This includes the calculation of
	 * the anomaly score. Transfers the tuple if it is an anomaly
	 *
	 * @param gId
	 *            The id if the group for this tuple
	 * @param sensorValue
	 *            The value of the sensor
	 * @param mean
	 *            The mean for that group
	 * @param standardDeviation
	 *            The standard deviation for that group
	 * @param tuple
	 *            The tuple which has to be transfered if it is an anomaly
	 * @param info
	 *            The deviationInformation for the group
	 *
	 */
	private void processTuple(Object gId, double sensorValue, T tuple, DeviationInformation info) {

		double mean = info.mean;
		double standardDeviation = info.standardDeviation;

		List<T> tuples = null;
		if (this.windowChecking) {
			tuples = tupleMap.get(gId);
			if (tuples == null) {
				tuples = new ArrayList<T>();
				tupleMap.put(gId, tuples);
			}
			removeOldValues(tuples, tuple.getMetadata().getStart(), info, exactCalculation, gId);
		}

		if (isAnomaly(sensorValue, standardDeviation, mean)) {

			if (this.windowChecking) {
				if (this.lastWindowWithAnomaly.get(gId)) {
					// Do not send the first tuples, only if they are in the
					// middle
					sendWindow(tuples, info);
				}
				this.lastWindowWithAnomaly.put(gId, true);
				this.lastAnomalies.put(gId, tuple);
			}

			double anomalyScore = calcAnomalyScore(sensorValue, mean, standardDeviation, interval);
			sendAnomaly(tuple, 0.0, anomalyScore, mean, standardDeviation);
		} else if (this.windowChecking) {
			// OK, we have a window (for checking, not for training)
			// This window should be a tumbling one (that would be good, not
			// necessary) e.g. 1 minute
			tuples.add(tuple);
		} else {
			// If we don't save tuples in a window, we can send the current
			// timestamp
			Heartbeat beat = Heartbeat.createNewHeartbeat(tuple.getMetadata().getStart());
			sendPunctuation(beat);
		}

	}

	private void sendAnomaly(T tuple, double punctuationDuration, double anomalyScore, double mean,
			double standardDeviation) {
		Object gId = this.groupProcessor.getGroupID(tuple);
		if (!this.onlyOnStart || (this.onlyOnStart && this.sendEnd.get(gId))) {
			this.sendEnd.put(gId, false);
			transferTuple(tuple, punctuationDuration, anomalyScore, mean, standardDeviation);
		}
	}

	/**
	 * Sends the tuples within a window if the window and the next window has an
	 * outlier - but does not send the tuples at the margins (which were no
	 * outliers and the window before / after had no outlier, too)
	 *
	 * @param tuples
	 * @param info
	 */
	private void sendWindow(List<T> tuples, DeviationInformation info) {
		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			T next = iter.next();
			Object gId = this.groupProcessor.getGroupID(next);
			T lastAnomaly = this.lastAnomalies.get(gId);
			if (lastAnomaly != null && next.getMetadata().getStart().before(lastAnomaly.getMetadata().getStart())) {
				iter.remove();
				continue;
			}
			double sensorValue = getValue(next);
			double anomalyScore = calcAnomalyScore(sensorValue, info.mean, info.standardDeviation, interval);
			// We need to avoid tuples with zero anomalyScore cause that would
			// mean that this is the end of the anomalies
			if (anomalyScore == 0.0) {
				anomalyScore += Double.MIN_VALUE;
			}
			sendAnomaly(next, 0.0, anomalyScore, info.mean, info.standardDeviation);
			iter.remove();
		}
	}

	/**
	 * Transfers a tuple to the next operator and appends the given information
	 *
	 * @param originalTuple
	 *            The tuple which needs to be enriched
	 * @param punctuationDuration
	 *            Necessary if "isTimeSensitive" is active. If not, you can put
	 *            anything in here, it won't be used.
	 * @param anomalyScore
	 *            Enrich the tuple with this anomaly score
	 * @param mean
	 *            Enrich the tuple with this mean
	 * @param standardDeviation
	 *            Enrich the tuple with this standard deviation
	 */
	private void transferTuple(Tuple originalTuple, double punctuationDuration, double anomalyScore, double mean,
			double standardDeviation) {
		Tuple newTuple = null;
		if (this.isTimeSensitive) {
			newTuple = originalTuple.append(punctuationDuration).append(anomalyScore).append(mean)
					.append(standardDeviation);
		} else {
			newTuple = originalTuple.append(anomalyScore).append(mean).append(standardDeviation);
		}
		transfer(newTuple);
	}

	/**
	 * Checks, if the given value is an anomaly with the interval set by the ao
	 *
	 * @param sensorValue
	 *            The value which we won't to know about if it's an anomaly
	 * @param standardDeviation
	 *            The standard deviation if the distribution
	 * @param mean
	 *            The mean of the distribution
	 * @return True, if it's an anomaly, false, if not
	 */
	private boolean isAnomaly(double sensorValue, double standardDeviation, double mean) {
		if (sensorValue < mean - (interval * standardDeviation)
				|| sensorValue > mean + (interval * standardDeviation)) {
			return true;
		}
		return false;
	}

	/**
	 * Calculates the anomaly score, a value between 0 and 1.
	 *
	 * @param distance
	 *            Distance to good area
	 * @return Anomaly score (0, 1]
	 */
	private double calcAnomalyScore(double sensorValue, double mean, double standardDeviation, double interval) {

		double minValue = mean - (interval * standardDeviation);
		double maxValue = mean + (interval * standardDeviation);

		double distance = sensorValue > maxValue ? sensorValue - maxValue
				: sensorValue < minValue ? minValue - sensorValue : 0;

		double div = distance / (maxValue - minValue);

		double addValue = 0.5;
		double anomalyScore = 0;
		while (div > 0 && !Double.isInfinite(div)) {
			if (div >= 1) {
				anomalyScore += addValue;
				addValue /= 2;
				div -= 1;
			} else {
				anomalyScore += div * addValue;
				div -= 1;
			}
		}

		if (Double.isInfinite(div)) {
			anomalyScore = 1;
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
		if (valueIndex >= 0) {
			double sensorValue = 0;
			if (getInputSchema(DATA_PORT).findAttribute(valueAttributeName).getDatatype().isInteger()) {
				Integer intValue = tuple.getAttribute(valueIndex);
				sensorValue = intValue.doubleValue();
			} else {
				sensorValue = tuple.getAttribute(valueIndex);
			}
			return sensorValue;
		}
		return 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

		if (this.isTimeSensitive) {
			for (Object groupId : deviationInfo.keySet()) {
				T lastDataTuple = this.lastDataTuples.get(groupId);
				if (lastDataTuple == null) {
					// Error if we have a group processor
					sendPunctuation(punctuation);
					return;
				}
				DeviationInformation info = getInfoForTuple(lastDataTuple);
				if (this.isTimeSensitive && info != null && lastDataTuple != null
						&& (this.lastTimePunctuationBasedTuple == null || punctuation.getTime()
								.minus(lastTimePunctuationBasedTuple).compareTo(new PointInTime(info.mean)) > 0)) {
					PointInTime time = punctuation.getTime().minus(lastDataTuple.getMetadata().getStart());
					if (time.getMainPoint() > info.mean) {
						// The next tuple takes longer than normal (if we look
						// for durations)
						if (isAnomaly(time.getMainPoint(), info.standardDeviation, info.mean)) {
							// Here we have an anomaly which did not really
							// occur, cause the source stopped sending
							this.lastTimePunctuationBasedTuple = punctuation.getTime();

							Tuple lastTuple = lastDataTuple;
							Tuple tuple = new Tuple(lastTuple);
							double anomalyScore = calcAnomalyScore(time.getMainPoint(), info.mean,
									info.standardDeviation, interval);
							transferTuple(tuple, time.getMainPoint(), anomalyScore, info.mean, info.standardDeviation);
						}
					}
				}
			}
		}

		sendPunctuation(punctuation);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	/**
	 * A small helper object to store the deviation information for the
	 * different groups
	 */
	class DeviationInformation {
		public double mean;
		public double standardDeviation;
		public int counter;
	}

}
