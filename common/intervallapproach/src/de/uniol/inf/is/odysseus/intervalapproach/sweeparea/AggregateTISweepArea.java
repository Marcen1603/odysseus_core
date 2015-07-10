package de.uniol.inf.is.odysseus.intervalapproach.sweeparea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class AggregateTISweepArea<T extends IStreamObject<? extends ITimeInterval>> {

	private class QueueComparator implements
			Comparator<IStreamObject<? extends ITimeInterval>>, Serializable {

		private static final long serialVersionUID = 5321893137959979782L;

		@Override
		public int compare(IStreamObject<? extends ITimeInterval> left,
				IStreamObject<? extends ITimeInterval> right) {
			return left.getMetadata().getStart()
					.compareTo(right.getMetadata().getStart());
		}

	}

	// private final PriorityQueue<T> elements;
	private final List<T> elements;
	private final QueueComparator comp = new QueueComparator();

	public AggregateTISweepArea() {
		// elements = new PriorityQueue<T>(11, new QueueComparator());
		elements = new LinkedList<T>();
	}

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

	public PointInTime calcMinTs() {
		if (elements.size() > 0) {
			return elements.get(0).getMetadata().getStart();
		} else {
			// // DEBUG: REMOVE AGAIN
			// PointInTime minTs = PointInTime.INFINITY;
			// synchronized (elements) {
			// Iterator<T> li = elements.iterator();
			// while (li.hasNext()) {
			// PointInTime toCompare = li.next().getMetadata().getStart();
			// if (toCompare.before(minTs)) {
			// minTs = toCompare;
			// }
			// }
			//
			// if (elements.size() > 0) {
			// PointInTime mintTs2 = elements.get(0).getMetadata().getStart();
			// if (mintTs2.equals(minTs)) {
			// return minTs;
			// } else {
			// System.err
			// .println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			// for (T e : clearSorted()) {
			// System.err.println(e.getMetadata().getStart());
			// }
			// System.err
			// .println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
			// }
			// }
		}
		return null;
	}

	public void insert(T value) {
		synchronized (elements) {
			int pos = Collections.binarySearch(elements, value, comp);
			if (pos < 0){
				elements.add((-(pos) - 1), value);
			}else{
				elements.add(pos,value);
			}			
//			elements.add(value);
//			// TODO: Maybe, this could be optimized. But mostly the collections are very small ...
//			Collections.sort(elements, comp);
		}
	}

	public void clear() {
		synchronized (elements) {
			elements.clear();
		}
	}
	
	public int size() {
		return elements.size();
	}

	public Iterator<T> iterator() {
		return elements.iterator();
		// List<T> sorted = new ArrayList<T>();
		// synchronized (elements) {
		// T elem;
		// while ((elem = elements.get(0)) != null) {
		// sorted.add(elem);
		// }
		// }
		// return sorted;
	}
}
