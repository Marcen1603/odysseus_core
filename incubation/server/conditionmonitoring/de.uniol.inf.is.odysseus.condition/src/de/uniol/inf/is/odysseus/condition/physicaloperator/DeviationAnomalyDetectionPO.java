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

	private String valueAttributeName;

	private double interval;
	private IGroupProcessor<T, T> groupProcessor;
	private Map<Long, DeviationInformation> deviationInfo;
	private Map<Long, List<T>> tupleMap;
	private boolean exactCalculation;

	private boolean oncePerWindow;
	private boolean alreadySendThisWindow;

	private boolean onlyOnChange;
	private boolean lastWindowWithAnomaly;
	private boolean foundAnomaly;

	private String standardDeviationAttributeName = "standardDeviation";
	private String meanAttributeName = "mean";

	// Testing purposes
	PointInTime lastTuple;

	public DeviationAnomalyDetectionPO(DeviationAnomalyDetectionAO ao, IGroupProcessor<T, T> groupProcessor) {
		this.interval = ao.getInterval();
		this.groupProcessor = groupProcessor;
		this.valueAttributeName = ao.getNameOfValue();
		this.deviationInfo = new HashMap<Long, DeviationInformation>();

		oncePerWindow = ao.isWindowChecking();
		alreadySendThisWindow = false;
		tupleMap = new HashMap<Long, List<T>>();

		onlyOnChange = ao.isOnlyOnChange();
		lastWindowWithAnomaly = false;
		foundAnomaly = false;
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

		if (port == 0 && info.wasWritten) {
			// Process data
			lastTuple = tuple.getMetadata().getStart();
			double sensorValue = getValue(tuple);
			processTuple(gId, sensorValue, tuple, info);
		} else if (port == 1) {
			// Update deviation information
			int stdDevIndex = getInputSchema(1).findAttributeIndex(standardDeviationAttributeName);
			if (stdDevIndex >= 0) {
				info.standardDeviation = tuple.getAttribute(stdDevIndex);
			}
			int meanIndex = getInputSchema(1).findAttributeIndex(meanAttributeName);
			if (meanIndex >= 0) {
				info.mean = tuple.getAttribute(meanIndex);
			}

			info.wasWritten = true;
		}
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
		boolean removedSomething = false;
		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			T next = iter.next();
			if (next.getMetadata().getEnd().beforeOrEquals(start)) {
				iter.remove();
				removedSomething = true;
			}
		}
		if (removedSomething) {
			if (this.oncePerWindow && this.alreadySendThisWindow) {
				// We delete something, the last tumbling window is history
				this.alreadySendThisWindow = false;
			}
			lastWindowWithAnomaly = foundAnomaly;
			foundAnomaly = false;
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
	private void processTuple(long gId, double sensorValue, T tuple, DeviationInformation info) {

		double mean = info.mean;
		double standardDeviation = info.standardDeviation;

		if (this.oncePerWindow) {
			// OK, we have a window (for checking, not for training)
			// This window should be a tumbling one, that would be good,
			// e.g. 1 minute
			List<T> tuples = tupleMap.get(gId);
			if (tuples == null) {
				tuples = new ArrayList<T>();
				tupleMap.put(gId, tuples);
			}
			tuples.add(tuple);
			removeOldValues(tuples, tuple.getMetadata().getStart(), info, exactCalculation);
		}

		if (isAnomaly(sensorValue, standardDeviation, mean)) {
			// Maybe here we have an anomaly
			if (!oncePerWindow || (oncePerWindow && !this.alreadySendThisWindow)) {
				// If we are in normal mode, send
				// If not (onePerWindow) only send if we did not already send
				// this window
				foundAnomaly = true;

				if (!onlyOnChange || (onlyOnChange && !lastWindowWithAnomaly)) {
					// If we only want to send if the last window had no
					// anomaly, we have to check this
					alreadySendThisWindow = true;
					double anomalyScore = calcAnomalyScore(sensorValue, mean, standardDeviation, interval);
					Tuple newTuple = tuple.append(anomalyScore);
					newTuple = newTuple.append(mean);
					newTuple = newTuple.append(standardDeviation);
					transfer(newTuple);
					return;
				}

				lastWindowWithAnomaly = true;
			}
		}

		Heartbeat beat = Heartbeat.createNewHeartbeat(tuple.getMetadata().getStart());
		sendPunctuation(beat);
	}

	private boolean isAnomaly(double sensorValue, double standardDeviation, double mean) {
		if (sensorValue < mean - (interval * standardDeviation) || sensorValue > mean + (interval * standardDeviation)) {
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

		double distance = sensorValue > maxValue ? sensorValue - maxValue : sensorValue < minValue ? minValue
				- sensorValue : 0;

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
			double sensorValue = tuple.getAttribute(valueIndex);
			return sensorValue;
		}
		return 0;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {

//		boolean timePunctuationSensitive = true;
//		DeviationInformation info = this.deviationInfo.get(0l);
//
//		if (timePunctuationSensitive && info != null) {
//			PointInTime time = punctuation.getTime().minus(lastTuple);
//			if (time.getMainPoint() > info.mean) {
//				// The next tuple takes longer than normal (if we look for
//				// durations)
//				if (isAnomaly(time.getMainPoint(), info.standardDeviation, info.mean)) {
//					// Here we have an anomaly which did not really occur, cause
//					// the source stopped sending
//					// System.out.println("This would be a new anomaly.");
//				}
//			}
//		}
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
		public boolean wasWritten = false;
		public double mean;
		public double standardDeviation;
	}

}
