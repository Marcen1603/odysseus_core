package de.uniol.inf.is.odysseus.anomalydetection.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.anomalydetection.logicaloperator.LOFAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;

/**
 * Operator that finds anomalies / outliers with the Local Outlier Factor
 * Algorithm.
 * 
 * @author Tobias Brandt
 *
 */
@SuppressWarnings("rawtypes")
public class LOFAnomalyDetectionPO<T extends Tuple<M>, M extends ITimeInterval> extends AbstractPipe<T, Tuple> {

	private String valueAttributeName;

	private Map<Object, List<T>> sortedValueMap;
	private int k;
	private double minLOFValue;
	private IGroupProcessor<T, T> groupProcessor;
	private boolean ignoreEqual;
	private boolean deliverFirstElements;

	public LOFAnomalyDetectionPO(LOFAnomalyDetectionAO ao, IGroupProcessor<T, T> groupProcessor) {
		sortedValueMap = new HashMap<>();
		this.k = ao.getNumberOfNeighbors();
		this.minLOFValue = ao.getLOFAnomalyValue();
		this.valueAttributeName = ao.getNameOfValue();
		this.groupProcessor = groupProcessor;
		this.ignoreEqual = ao.isIgnoreEqual();
		this.deliverFirstElements = ao.isDeliverFirstElement();
	}

	@Override
	protected void process_next(T tuple, int port) {

		// If set from the AO, use the group processor
		Object gId = groupProcessor.getGroupID(tuple);
		List<T> sortedValues = sortedValueMap.get(gId);

		// Use the right list for this group
		if (sortedValues == null) {
			sortedValues = new ArrayList<T>();
			sortedValueMap.put(gId, sortedValues);
		}

		sortedValues.add(tuple);
		removeOldValues(sortedValues, tuple.getMetadata().getStart());

		// If we have less tuples, we can't process the needed operations, as we
		// need at least k neighbors
		if (sortedValues.size() < k + 1 && !deliverFirstElements) {
			return;
		} else if (sortedValues.size() < k + 1 && deliverFirstElements) {
			// We can't process it anyway, but the user wants to see the first
			// elements
			Tuple newTuple = tuple.append(0.0).append(0.0);
			transfer(newTuple);
			return;
		}
		double lof = getLOF(k, tuple, sortedValues, this.ignoreEqual);

		// Check, if the LOF is higher than the maximum wanted by the user
		if (lof > this.minLOFValue) {
			Tuple newTuple = tuple.append(lof).append(calcAnomalyScore(lof, this.minLOFValue));
			transfer(newTuple);
			return;
		}
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(List<T> tuples, PointInTime start) {
		Iterator<T> iter = tuples.iterator();
		while (iter.hasNext()) {
			if (iter.next().getMetadata().getEnd().beforeOrEquals(start)) {
				iter.remove();
			}
		}
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

	/**
	 * Searches for the k nearest neighbors for the given value in the list of
	 * all values. Expects, that the list of all values is sorted.
	 * 
	 * @param k
	 *            Number of neighbors you want to have
	 * @param value
	 *            The value you search the neighbors for
	 * @return A list with the k nearest neighbors. The first value in the list
	 *         is the nearest neighbor
	 */
	private List<T> getKNearestNeighbors(int k, T value, List<T> neighbors, boolean ignoreEqual) {

		List<T> kNearest = new ArrayList<T>(k);
		int index = neighbors.indexOf(value);

		int kRunningLow = 1;
		int kRunningHigh = 1;

		while (kNearest.size() < k) {
			// Do this until we have k neighbors

			if (index - kRunningLow < 0) {
				// We can't go "down"
				kNearest = addToNeighborList(neighbors.get(index + kRunningHigh), kNearest, ignoreEqual);
				kRunningHigh++;
			} else if (index + kRunningHigh >= neighbors.size()) {
				// We can't go "up"
				kNearest = addToNeighborList(neighbors.get(index - kRunningLow), kNearest, ignoreEqual);
				kRunningLow++;
			} else if (Math.abs(getValue(neighbors.get(index - kRunningLow)) - getValue(value)) < Math
					.abs(getValue(neighbors.get(index + kRunningHigh)) - getValue(value))) {
				// The one under the value is nearer
				kNearest = addToNeighborList(neighbors.get(index - kRunningLow), kNearest, ignoreEqual);
				kRunningLow++;
			} else {
				// The one above the value is nearer
				kNearest = addToNeighborList(neighbors.get(index + kRunningHigh), kNearest, ignoreEqual);
				kRunningHigh++;
			}

			if ((index - kRunningLow < 0 && index + kRunningHigh >= neighbors.size()))
				// break if we went through the whole list (can occur if we
				// ignore equal values)
				break;
		}

		return kNearest;
	}

	private List<T> addToNeighborList(T tuple, List<T> neighbors, boolean ignoreEqual) {
		if (ignoreEqual && contains(neighbors, tuple))
			return neighbors;
		neighbors.add(tuple);
		return neighbors;
	}

	private boolean contains(List<T> list, T tuple) {

		// Go through the list from top to bottom, cause probably, if the
		// element is already in there, it was inserted just before this element
		for (int i = list.size() - 1; i >= 0; i--) {
			if (getValue(list.get(i)) == getValue(tuple))
				return true;
		}

		return false;
	}

	/**
	 * Calculates the distance to the k-nearest-neighbor.
	 * 
	 * @param k
	 *            The neighbor to search for
	 * @param value
	 *            The element you search the distance for
	 * @return The distance to the k'th neighbor
	 */
	private double getKDistance(int k, T tuple, List<T> neighbors, boolean ignoreEqual) {
		List<T> kNeighbors = getKNearestNeighbors(k, tuple, neighbors, ignoreEqual);
		T kNeighbor = kNeighbors.get(kNeighbors.size() - 1);
		double distance = Math.abs(getValue(kNeighbor) - getValue(tuple));
		return distance;
	}

	/**
	 * Calculates the reachabilityDistance. It's the maximum of the k-distance
	 * of the given neighbor and the distance of the given value to the neighbor
	 * 
	 * @param k
	 *            The number of neighbors to hop to calculate the k-distance
	 * @param value
	 *            The value you search the LOF for
	 * @param neighbour
	 *            The neighbor you want to know the distance for to the value.
	 * @return The reachability-distance.
	 */
	private double getReachabilityDistance(int k, T value, T neighbour, List<T> neighbors, boolean ignoreEqual) {
		double reachabilityDistance = Math.max(getKDistance(k, neighbour, neighbors, ignoreEqual),
				Math.abs(getValue(value) - getValue(neighbour)));
		return reachabilityDistance;
	}

	/**
	 * Calculates the local reachability distance. It's the average
	 * reachability-distance of the neighbors of the given value
	 * 
	 * @param k
	 *            The number of neighbors that need to be respected
	 * @param value
	 *            The value for which you want to calculate the neighborhood
	 * @return The local reachability density
	 */
	private double getLocalReachabilityDensity(int k, T value, List<T> allNeighbors, boolean ignoreEqual) {
		double sum = 0;
		List<T> neighbors = getKNearestNeighbors(k, value, allNeighbors, ignoreEqual);
		for (T neighbor : neighbors) {
			sum += getReachabilityDistance(k, value, neighbor, allNeighbors, ignoreEqual);
		}

		return 1 / (sum / neighbors.size());
	}

	/**
	 * Calculates the local outlier factor (LOF) for the given value respecting
	 * the k nearest neighbors.
	 * 
	 * @param k
	 *            The number of neighbors that need to be respected for the
	 *            calculation
	 * @param value
	 *            The element you want to get the LOF for
	 * @return The LOF for the given value
	 */
	private double getLOF(int k, T value, List<T> allNeighbors, boolean ignoreEqual) {
		Collections.sort(allNeighbors);

		double lrdSum = 0;
		for (T neighbor : getKNearestNeighbors(k, value, allNeighbors, ignoreEqual)) {
			lrdSum += getLocalReachabilityDensity(k, neighbor, allNeighbors, ignoreEqual);
		}
		double lrdLocal = getLocalReachabilityDensity(k, value, allNeighbors, ignoreEqual);

		if (Double.isInfinite(lrdSum) && Double.isInfinite(lrdLocal)) {
			// There are at least k + 1 duplicated, so this is no outlier
			return 0;
		}

		double lof = lrdSum / (k * lrdLocal);
		return lof;
	}

	/**
	 * Calculates the anomaly score, a value between 0 and 1.
	 * 
	 * @param distance
	 *            Distance to good area
	 * @return Anomaly score (0, 1]
	 */
	private double calcAnomalyScore(double lof, double minLOF) {

		// This should be at least 1 since we only to this if the lof > minLOF
		double div = lof / minLOF;

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
		sendPunctuation(punctuation);

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
}
