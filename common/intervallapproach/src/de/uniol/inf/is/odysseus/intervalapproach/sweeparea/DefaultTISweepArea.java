/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.intervalapproach.sweeparea;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.sweeparea.AbstractTimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.IFastList;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.TotallyBeforePredicate;

/**
 * This sweeparea implementation provides some optimizations on extract and
 * purge. The elements are always sorted and extract and purge only remove
 * elements, until the remove predicate first returns false. The remove
 * predicate is fixed to the TotallyBeforePredicate
 */
public class DefaultTISweepArea<T extends IStreamObject<? extends ITimeInterval>> extends AbstractTimeIntervalSweepArea<T> implements Comparable<DefaultTISweepArea<T>>, ITimeIntervalSweepArea<T> {

	Comparator<T> purgeComparator = new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			if (TimeInterval.totallyBefore(o1.getMetadata(), o2.getMetadata())) {
				return -1;
			}
			return 1;

		}
	};

	public DefaultTISweepArea() {
		super(new FastArrayList<T>(), new MetadataComparator<ITimeInterval>());
		super.setRemovePredicate(TotallyBeforePredicate.getInstance());
	}

	public DefaultTISweepArea(IFastList<T> list) {
		super(list, new MetadataComparator<ITimeInterval>());
		super.setRemovePredicate(TotallyBeforePredicate.getInstance());
	}

	public DefaultTISweepArea(DefaultTISweepArea<T> defaultTISweepArea) throws InstantiationException, IllegalAccessException {
		super(defaultTISweepArea);
	}

	public List<T> queryOverlapsAsList(ITimeInterval t) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			for (T s : getElements()) {
				if (TimeInterval.overlaps(s.getMetadata(), t)) {
					retval.add(s);
				}
			}
		}
		return retval;
	}

	public Iterator<T> queryOverlaps(ITimeInterval t) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			for (T s : getElements()) {
				if (TimeInterval.overlaps(s.getMetadata(), t)) {
					retval.add(s);
				}
			}
		}
		return retval.iterator();
	}

	public Iterator<T> extractOverlaps(ITimeInterval t) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> iter = getElements().iterator();
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

	public List<T> extractOverlapsAsList(ITimeInterval t) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> iter = getElements().iterator();
			while (iter.hasNext()) {
				T elem = iter.next();
				if (TimeInterval.overlaps(elem.getMetadata(), t)) {
					retval.add(elem);
					iter.remove();
				}
			}
		}
		return retval;
	}

	// public Iterator<T> queryElementsStartingBeforeReverse(PointInTime
	// validity) {
	// ArrayList<T> retval = new ArrayList<T>();
	// synchronized (getElements()) {
	// Iterator<T> iter = getElements().descendingIterator();
	// while (iter.hasNext()) {
	// T elem = iter.next();
	// if (elem.getMetadata().getStart().before(validity)) {
	// retval.add(elem);
	// }
	// }
	// }
	// return retval.iterator();
	// }

	public Iterator<T> queryElementsStartingBefore(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> iter = getElements().iterator();
			while (iter.hasNext()) {
				T elem = iter.next();
				if (elem.getMetadata().getStart().before(validity)) {
					retval.add(elem);
				}
			}
		}
		return retval.iterator();
	}

	/**
	 * Removes all elements from this sweep area that are totally before
	 * "element". The while loop in this method can be broken, if the next
	 * element has a start timestamp that is after or equals to the start
	 * timestamp of "element", because the elements in the sweep area are
	 * ordered by their start timestamps.
	 */
	@Override
	public void purgeElements(T element, Order order) {

		synchronized (getElements()) {

			if (getElements().size() > 10) {
				// System.err.println("Remove before " + element);
				// System.err.println("before remove " + getElements().size());
				// binary search returns
				// the index of the search key, if it is contained in the list;
				// otherwise, (-(insertion point) - 1). The insertion point is
				// defined as the point at which the key would be inserted into
				// the list: the index of the first element greater than the
				// key, or list.size() if all elements in the list are less than
				// the specified key. Note that this guarantees that the return
				// value will be >= 0 if and only if the key is found.
				// Remark: purgeComparator will never return equals!
				int delTo = Collections.binarySearch(getElements(), element, purgeComparator);
				// Only remove if before
				if (delTo < 0) {
					// find correct position
					delTo = (delTo + 1) * -1;
					if (delTo > 0) { // else there is nothing to do
						if (delTo < getElements().size()) {
							// System.err.println("remove until pos "
							// + (delTo - 1) + " "
							// + getElements().get(delTo - 1));

							getElements().removeRange(0, delTo - 1);
						} else {
							// System.err
							// .println("remove all --> Last element was "
							// + getElements().get(
							// getElements().size() - 1));
							getElements().clear();
						}
					}
				}
				// System.err.println("after remove " + getElements().size());
			} else {
				Iterator<T> it = this.getElements().iterator();
				// int i = 0;

				while (it.hasNext()) {
					T cur = it.next();
					if (getRemovePredicate().evaluate(cur, element)) {
						// ++i;
						it.remove();
					}

					if (cur.getMetadata().getStart().afterOrEquals(element.getMetadata().getStart())) {
						return;
					}

				}
			}
		}
	}

	@Override
	public Iterator<T> extractElements(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		synchronized (getElements()) {
			Iterator<T> it = this.getElements().iterator();
			while (it.hasNext()) {
				T next = it.next();
				if (getRemovePredicate().evaluate(next, element)) {
					it.remove();
					result.add(next);
				} else {
					return result.iterator();
				}
			}
		}
		return result.iterator();
	}

	/**
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITemporalSweepArea#extractElementsBefore(PointInTime)
	 */
	@Override
	public Iterator<T> extractElementsBefore(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// k�nnen (also davor liegen)
				if (TimeInterval.totallyBefore(s_hat.getMetadata(), validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	public Iterator<T> extractElementsStartingBefore(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// k�nnen (also davor liegen)
				if (s_hat.getMetadata().getStart().before(validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	public Iterator<T> extractElementsEndBefore(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (s_hat.getMetadata().getEnd().before(validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	public Iterator<T> extractElementsStartingEquals(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (s_hat.getMetadata().getStart().equals(validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	public List<T> extractEqualElementsStartingEquals(T element) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (s_hat.getMetadata().getStart().equals(element.getMetadata().getStart())) {
					if (s_hat.equals(element)) {
						retval.add(s_hat);
						li.remove();
					}
				} else {
					break;

				}
			}
		}
		return retval;
	}

	public Iterator<T> extractElementsStartingAfter(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (s_hat.getMetadata().getStart().after(validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	public Iterator<T> extractElementsStartingAfterOrEquals(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// k�nnen (also davor liegen)
				if (s_hat.getMetadata().getStart().afterOrEquals(validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	/**
	 * @return the min start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the first element in the
	 *         linked list.
	 */
	@Override
	public PointInTime getMinTs() {
		synchronized (getElements()) {
			if (!this.getElements().isEmpty()) {
				return this.getElements().get(0).getMetadata().getStart();
			}
		}
		return null;
	}

	/**
	 * 
	 * @return the max start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the last element in the
	 *         linked list.
	 */
	@Override
	public PointInTime getMaxTs() {
		if (!this.getElements().isEmpty()) {
			return this.getElements().get(getElements().size() - 1).getMetadata().getStart();
		}
		return null;
	}

	@Override
	public int compareTo(DefaultTISweepArea<T> other) {
		if (this.getMinTs().before(other.getMinTs())) {
			return -1;
		} else if (this.getMinTs().after(other.getMinTs())) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * For Debug-Purposes
	 * 
	 * @param baseTime
	 * @return the current Content of the SweepArea with baseTime as origin of
	 *         all points of time
	 */
	public String getSweepAreaAsString(PointInTime baseTime) {
		StringBuffer buf = new StringBuffer("SweepArea " + getElements().size() + " Elems \n");
		for (T element : getElements()) {
			buf.append(element).append(" ");
			buf.append("{META ").append(element.getMetadata().toString(baseTime)).append("}\n");
		}
		return buf.toString();
	}

	public String getSweepAreaAsString() {
		StringBuffer buf = new StringBuffer("SweepArea " + getElements().size() + " Elems \n");
		for (T element : getElements()) {
			buf.append(element).append(" ").append("}\n");
		}
		return buf.toString();
	}

	@Override
	public DefaultTISweepArea<T> clone() {
		try {
			return new DefaultTISweepArea<T>(this);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Clone error");
		}
	}

	/**
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITemporalSweepArea#purgeElementsBefore(PointInTime)
	 */
	@Override
	public void purgeElementsBefore(PointInTime time) {
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// k�nnen (also davor liegen)
				if (s_hat.getMetadata().getEnd().beforeOrEquals(time)) {
					li.remove();
				} else {
					break;
				}
			}
		}
	}

	/**
	 * Deliver all elements containing the time stamp. Sweep area is not changed
	 * 
	 * @param timestamp
	 * @return
	 */
	public Iterator<T> peekElementsContaing(PointInTime timestamp, boolean includingEndtime) {
		List<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (TimeInterval.inside(s_hat.getMetadata(), timestamp)) {
					retval.add(s_hat);
				}
				if (includingEndtime && s_hat.getMetadata().getEnd().equals(timestamp)) {
					retval.add(s_hat);
				}
			}
		}
		return retval.iterator();
	}

	public T peekLast() {
		return getElements().get(getElements().size());
	}

	public Iterator<T> extractAllElements() {
		LinkedList<T> result = new LinkedList<T>();
		synchronized (getElements()) {
			Iterator<T> it = this.getElements().iterator();
			while (it.hasNext()) {
				T next = it.next();
				it.remove();
				result.add(next);
			}
		}
		return result.iterator();
	}

	public void insertOrUpdate(T element, Comparator<T> comparator) {
		synchronized (getElements()) {
			synchronized (getElements()) {
				Iterator<T> it = this.getElements().iterator();
				while (it.hasNext()) {
					T next = it.next();
					if (comparator.compare(next, element) == 0) {
						it.remove();
					}
				}
				insert(element);
			}
		}
	}
}
