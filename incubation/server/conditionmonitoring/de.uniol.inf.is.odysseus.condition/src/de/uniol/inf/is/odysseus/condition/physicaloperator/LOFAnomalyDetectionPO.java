package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.logicaloperator.LOFAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

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

	private List<T> sortedValues;
	private int k;
	private double minLOFValue;

	public LOFAnomalyDetectionPO() {
		sortedValues = new ArrayList<T>();
		this.k = 10;
		this.minLOFValue = 1.5;
		this.valueAttributeName = "value";
	}

	public LOFAnomalyDetectionPO(LOFAnomalyDetectionAO ao) {
		sortedValues = new ArrayList<T>();
		this.k = ao.getNumberOfNeighbors();
		this.minLOFValue = ao.getLOFAnomalyValue();
		this.valueAttributeName = ao.getNameOfValue();
	}

	@Override
	protected void process_next(T tuple, int port) {
		sortedValues.add(tuple);
		if (sortedValues.size() < k + 1) {
			return;
		}
		double lof = getLOF(k, tuple);

		if (lof > this.minLOFValue) {
			Tuple newTuple = tuple.append(lof);
			transfer(newTuple);
			return;
		}

		removeOldValues(tuple.getMetadata().getStart());
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(PointInTime start) {
		Iterator<T> iter = sortedValues.iterator();
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
	private List<T> getKNearestNeighbors(int k, T value) {

		List<T> kNearest = new ArrayList<T>(k);
		int index = sortedValues.indexOf(value);

		int kRunningLow = 1;
		int kRunningHigh = 1;

		while (kNearest.size() < k) {

			if (index - kRunningLow < 0) {
				// We can't go "down"
				kNearest.add(sortedValues.get(index + kRunningHigh));
				kRunningHigh++;
			} else if (index + kRunningHigh >= sortedValues.size()) {
				// We can't go "up"
				kNearest.add(sortedValues.get(index - kRunningLow));
				kRunningLow++;
			} else if (Math.abs(getValue(sortedValues.get(index - kRunningLow)) - getValue(value)) < Math
					.abs(getValue(sortedValues.get(index + kRunningHigh)) - getValue(value))) {
				// The one under the value is nearer
				kNearest.add(sortedValues.get(index - kRunningLow));
				kRunningLow++;
			} else {
				// The one above the value is nearer
				kNearest.add(sortedValues.get(index + kRunningHigh));
				kRunningHigh++;
			}
		}

		return kNearest;
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
	private double getKDistance(int k, T tuple) {
		int index = sortedValues.indexOf(tuple);

		// Get distances from all values from index - 5 to index + 5
		List<Double> distances = new ArrayList<Double>(2 * k);
		for (int i = -k; i <= k; i++) {
			if (index + i < 0 || index + i >= sortedValues.size())
				continue;
			distances.add(Math.abs(getValue(sortedValues.get(index + i)) - getValue(tuple)));
		}

		// Sort by the distances
		Collections.sort(distances);

		// Get the k nearest (we can take the k'th and not the k'th - 1, cause
		// we have the distance to the item itself within this list)
		double kNearestValue = distances.get(k);
		return kNearestValue;
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
	private double getReachabilityDistance(int k, T value, T neighbour) {
		double reachabilityDistance = Math.max(getKDistance(k, neighbour),
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
	private double getLocalReachabilityDensity(int k, T value) {
		double sum = 0;
		List<T> neighbors = getKNearestNeighbors(k, value);
		for (T neighbor : neighbors) {
			sum += getReachabilityDistance(k, value, neighbor);
		}
		return neighbors.size() / sum;
	}

	/**
	 * Calculates the local outlier factor (LOF) for the given value respecting
	 * the k nearest neighbors.
	 * 
	 * @param k
	 *            The number of neighbors that need to be resprected for the
	 *            calculation
	 * @param value
	 *            The element you want to get the LOF for
	 * @return The LOF for the given value
	 */
	private double getLOF(int k, T value) {
		Collections.sort(sortedValues);

		double lrdSum = 0;
		for (T neighbor : getKNearestNeighbors(k, value)) {
			lrdSum += getLocalReachabilityDensity(k, neighbor);
		}
		double lof = lrdSum / (k * getLocalReachabilityDensity(k, value));
		return lof;

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
