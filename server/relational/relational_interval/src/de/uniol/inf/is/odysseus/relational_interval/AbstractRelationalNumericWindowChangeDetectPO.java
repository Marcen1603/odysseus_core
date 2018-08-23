package de.uniol.inf.is.odysseus.relational_interval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * This class bundles the window operations for the change detect operators that
 * use a window. You have to override the areDifferent method for your purpose.
 * 
 * @author Tobias Brandt
 *
 */
public class AbstractRelationalNumericWindowChangeDetectPO extends AbstractRelationalNumericChangeDetectPO {

	protected Map<Object, List<Tuple<?>>> windows;
	// The Integer is for the attributes to compare
	protected Map<Object, Map<Integer, Tuple<?>>> minValuesMap;
	protected Map<Object, Map<Integer, Tuple<?>>> maxValuesMap;

	public AbstractRelationalNumericWindowChangeDetectPO(int[] comparePositions, double tolerance) {
		super(comparePositions, tolerance);
		this.windows = new HashMap<>();
		this.minValuesMap = new HashMap<>();
		this.maxValuesMap = new HashMap<>();
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port) {
		List<Tuple<?>> window = null;
		Object groupID = null;
		if (groupProcessor != null) {
			groupID = groupProcessor.getGroupID(tuple);
		} else {
			groupID = 0L;
		}

		window = windows.get(groupID);

		if (window == null) {
			window = new ArrayList<Tuple<?>>();
			windows.put(groupID, window);
		}

		window.add(tuple);

		Map<Integer, Tuple<?>> minValues = minValuesMap.get(groupID);
		Map<Integer, Tuple<?>> maxValues = maxValuesMap.get(groupID);

		if (minValues == null) {
			minValues = new HashMap<Integer, Tuple<?>>();
			minValuesMap.put(groupID, minValues);
		}
		if (maxValues == null) {
			maxValues = new HashMap<Integer, Tuple<?>>();
			maxValuesMap.put(groupID, maxValues);
		}

		// Remove old values from the window
		Object metaObject = tuple.getMetadata();
		if (metaObject instanceof TimeInterval) {
			TimeInterval time = (TimeInterval) metaObject;
			removeOldValues(window, time.getStart(), groupID);
		}

		boolean areSignificantlyDifferent = false;

		// Compare with the smallest and biggest value for each dimension
		for (int dimension : this.comparePositions) {
			Tuple<?> minTuple = minValues.get(dimension);
			Tuple<?> maxTuple = maxValues.get(dimension);

			if (minTuple == null) {
				// We don't have a minimal Value, we have to search for the
				// right one
				minTuple = getMinTuple(window, dimension);
			}

			if (((Number) tuple.getAttribute(dimension)).doubleValue() <= ((Number) minTuple.getAttribute(dimension))
					.doubleValue()) {
				// The current tuple is smaller or equal, take this as minValue
				// for this dimension
				minValues.put(dimension, tuple);
				minTuple = tuple;
			}

			if (maxTuple == null) {
				maxTuple = getMaxTuple(window, dimension);
			}

			if (((Number) tuple.getAttribute(dimension)).doubleValue() >= ((Number) maxTuple.getAttribute(dimension))
					.doubleValue()) {
				// The current tuple is smaller or equal, take this as minValue
				// for this dimension
				maxValues.put(dimension, tuple);
				maxTuple = tuple;
			}

			if (areDifferent(tuple, minTuple) || areDifferent(tuple, maxTuple)) {
				areSignificantlyDifferent = true;
				// We have to go on with the loop, because this tuple may be the
				// smallest or biggest tuple in another dimension and we have to
				// know that
			}
		}

		if (tuple != null && areSignificantlyDifferent || (deliverFirstElement && window.size() == 1)) {
			// Tuples are different or it's the first tuple of the group and the
			// user wants
			// to get it
			transferInternal(tuple, 0);
		} else {
			heartbeatGenerationStrategy.generateHeartbeat(tuple, this);
			suppressedElements++;
			transferInternal(tuple, 1);
		}
	}

	/**
	 * Searches for the maximum tuple for the given dimension (attribute to
	 * compare) in the given list
	 * 
	 * @param tuples
	 *            The list of tuples, typically the window
	 * @param dimension
	 *            The attribute to compare
	 * @return The tuple with the highest value for the given dimension
	 */
	private Tuple<?> getMaxTuple(List<Tuple<?>> tuples, int dimension) {
		Tuple<?> maxTuple = null;
		for (Tuple<?> tuple : tuples) {
			if (maxTuple == null) {
				maxTuple = tuple;
			} else if (((Number) tuple.getAttribute(dimension)).doubleValue() >= ((Number) maxTuple
					.getAttribute(dimension)).doubleValue()) {
				maxTuple = tuple;
			}
		}

		return maxTuple;
	}

	/**
	 * Searches for the minimum tuple for the given dimension (attribute to
	 * compare) in the given list
	 * 
	 * @param tuples
	 *            The list of tuples, typically the window
	 * @param dimension
	 *            The attribute to compare
	 * @return The tuple with the lowest value for the given dimension
	 */
	private Tuple<?> getMinTuple(List<Tuple<?>> tuples, int dimension) {
		Tuple<?> minTuple = null;
		for (Tuple<?> tuple : tuples) {
			if (minTuple == null) {
				minTuple = tuple;
			} else if (((Number) tuple.getAttribute(dimension)).doubleValue() <= ((Number) minTuple
					.getAttribute(dimension)).doubleValue()) {
				minTuple = tuple;
			}
		}

		return minTuple;
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(List<Tuple<?>> tuples, PointInTime start, Object gId) {

		Map<Integer, Tuple<?>> minValues = this.minValuesMap.get(gId);
		Map<Integer, Tuple<?>> maxValues = this.maxValuesMap.get(gId);

		Iterator<Tuple<?>> iter = tuples.iterator();
		while (iter.hasNext()) {
			Tuple<?> nextTuple = iter.next();
			Object metaObject = nextTuple.getMetadata();
			if (metaObject instanceof TimeInterval) {
				TimeInterval time = (TimeInterval) metaObject;
				if (time.getEnd().beforeOrEquals(start)) {

					// Remove the min or max values, if they fall out of the
					// window
					for (Integer key : minValues.keySet()) {
						if (minValues.get(key) == nextTuple) {
							minValues.remove(key);
						}
					}

					for (Integer key : maxValues.keySet()) {
						if (maxValues.get(key) == nextTuple) {
							maxValues.remove(key);
						}
					}

					iter.remove();
				}
			}
		}
	}

}