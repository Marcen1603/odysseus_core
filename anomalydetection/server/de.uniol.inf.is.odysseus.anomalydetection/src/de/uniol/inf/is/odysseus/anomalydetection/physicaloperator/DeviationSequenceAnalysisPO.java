package de.uniol.inf.is.odysseus.anomalydetection.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.anomalydetection.datatypes.DeviationInformation;
import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.DeviationSequenceAnalysisAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

@SuppressWarnings("rawtypes")
public class DeviationSequenceAnalysisPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private static final int DATA_PORT = 0;
	private static final int LEARN_PORT_SINGLE_TUPLE = 1;
	// private static final int LEARN_PORT_CURVE = 2;

	// For analysis
	// groupId, PointInTime from when it's active and information
	private Map<Object, Map<PointInTime, DeviationInformation>> deviationInfo;
	private double interval;
	private long lastCounter;
	private double totalSum;
	private double meanSum;

	// Learn attributes
	private String meanAttributeName;
	private String standardDeviationAttributeName;

	// Data attributes
	private String valueAttributeName;

	// For tuples where the info is too old
	private List<T> futureTupleList;

	protected IGroupProcessor<T, T> groupProcessor;

	public DeviationSequenceAnalysisPO(DeviationSequenceAnalysisAO ao, IGroupProcessor<T, T> groupProcessor) {
		this.deviationInfo = new HashMap<>();
		this.futureTupleList = new ArrayList<>();

		this.interval = ao.getInterval();

		this.meanAttributeName = ao.getMeanAttributeName();
		this.standardDeviationAttributeName = ao.getStandardDeviationAttributeName();

		this.valueAttributeName = ao.getValueAttributeName();

		this.groupProcessor = groupProcessor;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		if (groupProcessor != null) {
			groupProcessor.init();
		}
	}

	@Override
	protected void process_next(T tuple, int port) {
		Long sequenceCounter = this.groupProcessor.getAscendingGroupID(tuple);
		Map<PointInTime, DeviationInformation> infoMap = this.deviationInfo.get(sequenceCounter);

		if (infoMap == null) {
			// First occurrence of this group
			infoMap = new HashMap<PointInTime, DeviationInformation>();
			this.deviationInfo.put(sequenceCounter, infoMap);
		}

		if (port == DATA_PORT) {
			// Process data
			DeviationInformation info = getInfoForTuple(tuple);
			if (info == null) {
				// We don't have information about this yet, hence, we can't
				// say anything. (Probably we did not get any deviation
				// information yet). Therefore save it for
				// later
				futureTupleList.add(tuple);
				return;
			}
			processTuple(sequenceCounter, tuple, info);

		} else if (port == LEARN_PORT_SINGLE_TUPLE) {
			// Update deviation information
			updateDeviationInfo(tuple, infoMap);

			// With this new knowledge maybe we can process a few tuples we
			// weren't able to before
			List<T> toRemove = new ArrayList<>();
			for (T oldTuple : futureTupleList) {
				// Get correct information
				DeviationInformation correctInfo = getInfoForTuple(oldTuple);
				if (correctInfo == null) {
					if (existsNewerInfo(oldTuple)) {
						// Well, we will never get the correct info as we are
						// beyond this tuple. Maybe the tuple was the first
						// tuple before there was any information. Hence, we can
						// delete it.
						toRemove.add(oldTuple);
					}
				}
				if (correctInfo != null) {
					processTuple(sequenceCounter, oldTuple, correctInfo);
					toRemove.add(oldTuple);
				}
			}
			futureTupleList.removeAll(toRemove);
		}
	}

	private void processTuple(Long sequenceCounter, T tuple, DeviationInformation info) {
		if (lastCounter > sequenceCounter) {
			// A new sequence starts, the last one is finished
			// We can now send the info about the last sequence
			double totalDifference = Math.abs(totalSum - meanSum);

			Tuple<ITimeInterval> output = new Tuple<ITimeInterval>(6, false);
			output.setMetadata(tuple.getMetadata());
			output.setAttribute(0, totalSum);
			output.setAttribute(1, meanSum);
			output.setAttribute(2, totalDifference);
			output.setAttribute(3, totalDifference / meanSum);
			output.setAttribute(4, lastCounter);
			output.setAttribute(5, totalSum / lastCounter);

			transfer(output, 1);

			totalDifference = 0;
			totalSum = 0;
			meanSum = 0;
		}

		lastCounter = sequenceCounter;

		totalSum += getValue(tuple);
		meanSum += info.mean;

		if (isAnomaly(getValue(tuple), info.standardDeviation, info.mean)) {
			// We have an anomaly for this tuple
			double anomalyScore = calcAnomalyScore(getValue(tuple), info.mean, info.standardDeviation, this.interval);
			Tuple newTuple = tuple.append(anomalyScore).append(info.mean).append(info.standardDeviation);
			transfer(newTuple);
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
	 * Checks, if there is an info which is newer than the given tuple
	 * 
	 * @param tuple
	 *            The tuple to compare the timestamps to the infos
	 * @return true, if there is a newer info and false, if not
	 */
	private boolean existsNewerInfo(T tuple) {
		Object gId = groupProcessor.getGroupID(tuple);
		Map<PointInTime, DeviationInformation> infoMap = this.deviationInfo.get(gId);
		PointInTime tupleStartTime = tuple.getMetadata().getStart();

		// Search for the newest information which fits
		PointInTime newestPointInTime = null;
		for (PointInTime startTime : infoMap.keySet()) {
			if ((newestPointInTime == null || startTime.after(newestPointInTime))) {
				newestPointInTime = startTime;
			}
		}

		if (newestPointInTime.after(tupleStartTime)) {
			return true;
		}

		return false;
	}

	private boolean isAnomaly(double sensorValue, double standardDeviation, double mean) {
		if (sensorValue < mean - (interval * standardDeviation)
				|| sensorValue > mean + (interval * standardDeviation)) {
			return true;
		}
		return false;
	}

	/**
	 * Helper function to easily get the value in the attribute specified in the
	 * valueAttributeName variable via the AO
	 * 
	 * @param tuple
	 *            The tuple where this attribute is in
	 * @return The double value in the tuple
	 */
	protected double getValue(T tuple) {
		int valueIndex = getInputSchema(DATA_PORT).findAttributeIndex(valueAttributeName);
		if (valueIndex >= 0) {
			double sensorValue = tuple.getAttribute(valueIndex);
			return sensorValue;
		}
		return 0;
	}

	/**
	 * Updates the deviation info that belongs to the given tuple
	 * 
	 * @param tuple
	 *            The tuple with which the deviation info should be updated
	 * @return The updated deviation info
	 */
	private DeviationInformation updateDeviationInfo(T tuple, Map<PointInTime, DeviationInformation> infoMap) {
		DeviationInformation newInformation = new DeviationInformation();
		infoMap.put(tuple.getMetadata().getStart(), newInformation);

		int meanIndex = getInputSchema(LEARN_PORT_SINGLE_TUPLE).findAttributeIndex(meanAttributeName);
		int stdDevIndex = getInputSchema(LEARN_PORT_SINGLE_TUPLE).findAttributeIndex(standardDeviationAttributeName);
		if (meanIndex >= 0 && stdDevIndex >= 0) {
			double mean = tuple.getAttribute(meanIndex);
			double stdDev = tuple.getAttribute(stdDevIndex);
			newInformation.mean = mean;
			newInformation.standardDeviation = stdDev;

		}

		return newInformation;
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

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

}
