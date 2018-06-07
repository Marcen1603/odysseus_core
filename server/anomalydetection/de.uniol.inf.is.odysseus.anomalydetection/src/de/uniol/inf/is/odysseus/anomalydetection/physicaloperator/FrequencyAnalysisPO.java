package de.uniol.inf.is.odysseus.anomalydetection.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.FrequencyAnalysisAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * This operator compares the occurrence frequency of tuples (using the
 * equal-method of the tuples) in two different windows. The first window (on
 * port 0) has to be bigger or equal in size than the second window (on port 1).
 * The relative occurrence frequency of the incoming tuple in the small and the
 * big window is compared. If it's significant different (with the tolerance the
 * user chose) an anomaly tuple will be generated.
 * 
 * The frequency-value: A value smaller than 1 means, that the tuple occurred
 * less frequent (seldom) in the small window (compared to the big window). A
 * value bigger than 1 means, that the tuple occurred more often in the small
 * window.
 * 
 * @author Tobias Brandt
 */
@SuppressWarnings("rawtypes")
public class FrequencyAnalysisPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private List<T> bigWindow;
	private List<T> smallWindow;

	private Map<T, Long> bigCounter;
	private Map<T, Long> smallCounter;

	// The tolerance values from the user (e.g. both = 0.3)
	private double toleranceNegative;
	private double tolerancePositive;
	
	private boolean deliverFirstElements;

	public FrequencyAnalysisPO(FrequencyAnalysisAO ao) {
		this.bigWindow = new ArrayList<T>();
		this.smallWindow = new ArrayList<T>();

		this.bigCounter = new HashMap<T, Long>();
		this.smallCounter = new HashMap<T, Long>();
		
		this.toleranceNegative = ao.getToleranceNegative();
		this.tolerancePositive = ao.getTolerancePositive();
		
		this.deliverFirstElements = ao.isDeliverFirstElements();
	}

	@Override
	protected void process_next(T tuple, int port) {

		// Add to window and count, what we got here
		if (port == 0) {
			this.bigWindow.add(tuple);
			Long counterBig = this.bigCounter.get(tuple);
			if (counterBig == null)
				counterBig = 1l;
			else
				counterBig++;
			this.bigCounter.put(tuple, counterBig);

			// Remove old values
			removeOldValues(this.bigWindow, this.bigCounter, tuple.getMetadata().getStart());
		} else if (port == 1) {
			this.smallWindow.add(tuple);
			Long counterSmall = this.smallCounter.get(tuple);
			if (counterSmall == null)
				counterSmall = 1l;
			else
				counterSmall++;
			this.smallCounter.put(tuple, counterSmall);

			// Remove old values
			removeOldValues(this.smallWindow, this.smallCounter, tuple.getMetadata().getStart());
		}

		// Do not compare every tuple twice
		if (port == 1 && (this.bigWindow.size() >= this.smallWindow.size() || this.deliverFirstElements )) {
			// Compare the frequencies for this tuple
			double compare = compareFrequencies(tuple, this.bigCounter, this.smallCounter);

			if (compare < 1.0 - toleranceNegative || compare > 1.0 + tolerancePositive) {
				// There's a remarkable difference
				double anomalyScore = calcAnomalyScore(compare, this.toleranceNegative, this.tolerancePositive);
				Tuple newTuple = tuple.append(compare).append(anomalyScore);
				transfer(newTuple);
				return;
			}
		}

	}

	/**
	 * Compares the relative frequency of the tuple in both maps. Divides the
	 * relative frequency of the tuple in the small map by the relative
	 * frequency in the big map.
	 * 
	 * A value smaller than 1 means, that the tuple occurred less frequent
	 * (seldom) in the small map. A value bigger than 1 means, that the tuple
	 * occurred more often in the small map.
	 * 
	 * @param tuple
	 *            The tuple you want to know the frequency of
	 * @param bigCounterMap
	 *            The big counter map (from the big window)
	 * @param smallCounterMap
	 *            The small counter map (from the small window)
	 * @return The compared frequency
	 */
	private double compareFrequencies(T tuple, Map<T, Long> bigCounterMap, Map<T, Long> smallCounterMap) {
		// Get relative frequency from small and big map
		double smallFrequency = getRelativeFrequency(tuple, smallCounterMap);
		double bigFrequency = getRelativeFrequency(tuple, bigCounterMap);

		return smallFrequency / bigFrequency;
	}

	/**
	 * Calculates the relative frequency of the given tuple in the given map
	 * (just divides the counter of the tuple by the total count of the map)
	 * 
	 * @param tuple
	 *            The tuple you need the relative frequency for
	 * @param counterMap
	 *            The map you want to compare the counter of the tuple to
	 * @return The relative frequency of the tuple in comparisson to the whole
	 *         map
	 */
	private double getRelativeFrequency(T tuple, Map<T, Long> counterMap) {
		Long ownCountObj = counterMap.get(tuple);
		long ownCount = 1l;
		if (ownCountObj != null)
			ownCount = ownCountObj;

		Long totalCountObj = getTotalCount(counterMap);
		long totalCount = 1;
		if (totalCountObj != null)
			totalCount = totalCountObj;

		if ((ownCountObj == null) != (totalCountObj == null))
			return 0;

		return (double) ownCount / totalCount;
	}

	/**
	 * Calculates the total count of a map -> sums up the counts of all values
	 * 
	 * @param map
	 *            The map you want to know the total count for
	 * @return The total count of the map
	 */
	private long getTotalCount(Map<T, Long> map) {
		long count = 0l;
		for (T tuple : map.keySet()) {
			count += map.get(tuple);
		}
		return count;
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(List<T> tuples, Map<T, Long> counterMap, PointInTime start) {
		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			T tuple = iter.next();
			if (tuple.getMetadata().getEnd().beforeOrEquals(start)) {

				// Decrease the counter
				Long counter = counterMap.get(tuple);
				if (counter != null)
					counter--;
				counterMap.put(tuple, counter);

				// Remove tuple
				iter.remove();
			}
		}
	}

	/**
	 * Calculates the anomaly score, a value between 0 and 1. 
	 * 
	 * @param distance
	 *            Distance to good area
	 * @return Anomaly score (0, 1]
	 */
	private double calcAnomalyScore(double compareValue, double toleranceNegative, double tolerancePositive) {

		if (Double.isInfinite(compareValue))
			return 1;
		
		double minValue = 1 - toleranceNegative;
		double maxValue = 1 + tolerancePositive;

		double distance = compareValue > maxValue ? compareValue - maxValue : compareValue < minValue ? minValue
				- compareValue : 0;

		double div = distance / (maxValue - minValue);

		double calcTolerance = toleranceNegative;
		if (compareValue > maxValue)
			calcTolerance = tolerancePositive;
		
		double addValue = 0.5;
		double anomalyScore = 0;
		while (div > 0) {
			if (div >= calcTolerance) {
				anomalyScore += addValue;
				addValue /= 2;
				div -= calcTolerance;
			} else {
				anomalyScore += div * addValue;
				div -= calcTolerance;
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
		return OutputMode.MODIFIED_INPUT;
	}

}
