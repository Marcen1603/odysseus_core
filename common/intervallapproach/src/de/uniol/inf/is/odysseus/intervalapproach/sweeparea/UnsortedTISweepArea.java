package de.uniol.inf.is.odysseus.intervalapproach.sweeparea;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class UnsortedTISweepArea<T extends IStreamObject<? extends ITimeInterval>> {

	private final List<T> elements = new LinkedList<T>();

	public Iterator<T> extractElementsBefore(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (elements) {
			Iterator<T> li = elements.iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (TimeInterval.totallyBefore(s_hat.getMetadata(), validity)) {
					retval.add(s_hat);
					li.remove();
				}
			}
		}
		return retval.iterator();
	}
	
	public Iterator<T> extractOverlaps(ITimeInterval t) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (elements) {
			Iterator<T> iter = elements.iterator();
			while (iter.hasNext()) {
				T elem = iter.next();
				if (TimeInterval.overlaps(elem.getMetadata(), t)) {
					retval.add(elem);
					iter.remove();
				}
			}
		}
		return retval.iterator();
	}


	/**
	 * For cases where sweep areas are not sorted by time, calculate the new
	 * minTS
	 * 
	 * @return
	 */
	public PointInTime calcMinTs() {
		synchronized (elements) {
			if (elements.size() > 0) {
				Iterator<T> iter = elements.iterator();
				PointInTime minTs = iter.next().getMetadata().getStart();
				while (iter.hasNext()) {
					PointInTime next = iter.next().getMetadata().getStart();
					if (minTs.after(next)) {
						minTs = next;
					}
				}
				return minTs;
			}
		}
		return null;
	}

	public void insert(T value){
		elements.add(value);
	}
	
	public void clear() {
		elements.clear();
	}

	public Iterator<T> iterator() {
		return elements.iterator();
	}
}
