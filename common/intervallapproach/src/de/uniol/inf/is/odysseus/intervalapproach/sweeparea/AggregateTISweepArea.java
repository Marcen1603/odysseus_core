package de.uniol.inf.is.odysseus.intervalapproach.sweeparea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaElementSet;

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

	private final PriorityQueue<T> elements = new PriorityQueue<T>(11,new QueueComparator());

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
		if (elements.size() > 0){
			return elements.peek().getMetadata().getStart();
		}
		return null;
	}

	public void insert(T value) {
		elements.add(value);
	}

	public void clear() {
		elements.clear();
	}

	public List<T> clearSorted() {
		List<T> sorted = new ArrayList<T>();
		synchronized (elements) {
			T elem;
			while((elem = elements.poll()) != null){
				sorted.add(elem);
			}
		}
		return sorted;
	}
}
