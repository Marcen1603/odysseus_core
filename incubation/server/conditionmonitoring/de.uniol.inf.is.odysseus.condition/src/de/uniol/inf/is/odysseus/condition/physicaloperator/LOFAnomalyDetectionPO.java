package de.uniol.inf.is.odysseus.condition.physicaloperator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.logicaloperator.LOFAnomalyDetectionAO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
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
public class LOFAnomalyDetectionPO<T extends Tuple<?>> extends AbstractPipe<T, Tuple> {

	public static final String VALUE_NAME = "value";

	private List<Double> values;
	private int k;
	private double minLOFValue;

	public LOFAnomalyDetectionPO() {
		values = new ArrayList<Double>();
		this.k = 3;
		this.minLOFValue = 1.5;
	}

	public LOFAnomalyDetectionPO(LOFAnomalyDetectionAO ao) {
		values = new ArrayList<Double>();
		this.k = ao.getNumberOfNeighbors();
		this.minLOFValue = ao.getLOFAnomalyValue();
	}

	@Override
	protected void process_next(T tuple, int port) {

		int valueIndex = getOutputSchema().findAttributeIndex(VALUE_NAME);
		double sensorValue = tuple.getAttribute(valueIndex);

		values.add(sensorValue);
		if (values.size() < k + 1)
			return;

		double lof = getLOF(k, sensorValue);

		if (lof > this.minLOFValue) {
			Tuple newTuple = tuple.append(lof);
			transfer(newTuple);
			return;
		}
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
	private List<Double> getKNearestNeighbors(int k, double value) {

		List<Double> kNearest = new ArrayList<Double>(k);
		int index = values.indexOf(value);

		int kRunningLow = 1;
		int kRunningHigh = 1;

		while (kNearest.size() < k) {

			if (index - kRunningLow < 0) {
				// We can't go "down"
				kNearest.add(values.get(index + kRunningHigh));
				kRunningHigh++;
			} else if (index + kRunningHigh >= values.size()) {
				// We can't go "up"
				kNearest.add(values.get(index - kRunningLow));
				kRunningLow++;
			} else if (Math.abs(values.get(index - kRunningLow) - value) < Math.abs(values.get(index + kRunningHigh)
					- value)) {
				// The one under the value is nearer
				kNearest.add(values.get(index - kRunningLow));
				kRunningLow++;
			} else {
				// The one above the value is nearer
				kNearest.add(values.get(index + kRunningHigh));
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
	private double getKDistance(int k, double value) {
		int index = values.indexOf(value);

		// Get distances from all values from index - 5 to index + 5
		List<Double> distances = new ArrayList<Double>(2 * k);
		for (int i = -k; i <= k; i++) {
			if (index + i < 0 || index + i >= values.size())
				continue;
			distances.add(Math.abs(values.get(index + i) - value));
		}

		// Sort by the distances
		Collections.sort(distances);

		// Get the k nearest (we can take the k'th and not the k'th - 1, cause
		// we have the distance to the item itself within this list
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
	private double getReachabilityDistance(int k, double value, double neighbour) {
		double reachabilityDistance = Math.max(getKDistance(k, neighbour), Math.abs(value - neighbour));
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
	private double getLocalReachabilityDensity(int k, double value) {
		double sum = 0;
		List<Double> neighbors = getKNearestNeighbors(k, value);
		for (double neighbor : neighbors) {
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
	private double getLOF(int k, double value) {
		Collections.sort(values);
		
		double lrdSum = 0;
		for (double neighbor : getKNearestNeighbors(k, value)) {
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
