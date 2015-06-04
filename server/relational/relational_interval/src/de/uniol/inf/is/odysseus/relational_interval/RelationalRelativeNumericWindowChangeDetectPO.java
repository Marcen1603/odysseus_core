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
 * This operator checks, if there is a relative change within that window.
 * Therefore, the oldest tuple in the window is compared to the new incoming
 * tuple.
 * 
 * @author Tobias Brandt
 *
 */
public class RelationalRelativeNumericWindowChangeDetectPO extends RelationalRelativeNumericChangeDetectPO {

	private Map<Long, List<Tuple<?>>> windows;

	public RelationalRelativeNumericWindowChangeDetectPO(int[] comparePositions, double tolerance) {
		super(comparePositions, tolerance);
		this.windows = new HashMap<Long, List<Tuple<?>>>();
	}

	public RelationalRelativeNumericWindowChangeDetectPO(RelationalRelativeNumericWindowChangeDetectPO po) {
		super(po);
		this.windows = new HashMap<Long, List<Tuple<?>>>();
	}

	@Override
	protected void process_next(Tuple<?> tuple, int port) {
		List<Tuple<?>> window = null;
		Long groupID = null;
		if (groupProcessor != null) {
			groupID = groupProcessor.getGroupID(tuple);
			window = windows.get(groupID);
		} else {
			groupID = 0L;
			window = windows.get(groupID);
		}

		if (window == null) {
			window = new ArrayList<Tuple<?>>();
			windows.put(groupID, window);
		}

		// Add new tuple to the window
		window.add(tuple);

		// Remove old values from the window
		Object metaObject = tuple.getMetadata();
		if (metaObject instanceof TimeInterval) {
			TimeInterval time = (TimeInterval) metaObject;
			removeOldValues(window, time.getStart());
		}

		// The element to compare is the oldest element in the window
		Tuple<?> compareElement = window.get(0);
		if ((tuple != null && areDifferent(tuple, compareElement)) || (deliverFirstElement && window.size() == 1)) {
			// Tuples are different or it's the first tuple and the user wants
			// to get it
			transferInternal(tuple);
		} else {
			heartbeatGenerationStrategy.generateHeartbeat(tuple, this);
			suppressedElements++;
		}
	}

	/**
	 * Removes the old values from the operator internal storage due to the
	 * timestamps set by the window
	 * 
	 * @param start
	 *            Start timestamp from the newest tuple
	 */
	private void removeOldValues(List<Tuple<?>> tuples, PointInTime start) {
		Iterator<Tuple<?>> iter = tuples.iterator();
		while (iter.hasNext()) {
			Object metaObject = iter.next().getMetadata();
			if (metaObject instanceof TimeInterval) {
				TimeInterval time = (TimeInterval) metaObject;
				if (time.getEnd().beforeOrEquals(start)) {
					iter.remove();
				}
			}
		}
	}

}
