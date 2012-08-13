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
package de.uniol.inf.is.odysseus.intervalapproach;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.AbstractSweepArea;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.TotallyBeforePredicate;

/**
 * This sweeparea implementation provides some optimizations on extract and
 * purge. The elements are always sorted and extract and purge only remove
 * elements, until the remove predicate first returns false. The remove
 * predicate is fixed to the TotallyBeforePredicate
 */
public class DefaultTISweepArea<T extends IMetaAttributeContainer<? extends ITimeInterval>> extends AbstractSweepArea<T> implements Comparable<DefaultTISweepArea<T>>,
		ITimeIntervalSweepArea<T> {
	// private static final Logger logger =
	// LoggerFactory.getLogger(ITemporalSweepArea.class);

	public DefaultTISweepArea() {
		super(new MetadataComparator<ITimeInterval>());
		super.setRemovePredicate(TotallyBeforePredicate.getInstance());
	}

	public DefaultTISweepArea(DefaultTISweepArea<T> defaultTISweepArea) {
		super(defaultTISweepArea);
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

	public Iterator<T> queryElementsStartingBeforeFIFO(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> iter = getElements().descendingIterator();
			while (iter.hasNext()) {
				T elem = iter.next();
				if (elem.getMetadata().getStart().before(validity)) {
					retval.add(elem);
				}
			}
		}
		return retval.iterator();
	}

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
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITemporalSweepArea#extractElementsBefore(PointInTime)
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

	public Iterator<T> extractElementsStartingEquals(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				// Alle Elemente entfernen, die nicht mehr verschnitten werden
				// k�nnen (also davor liegen)
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

	/**
	 * @return the min start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the first element in the
	 *         linked list.
	 */
	@Override
	public PointInTime getMinTs() {
		synchronized (getElements()) {
			if (!this.getElements().isEmpty()) {
				return this.getElements().peek().getMetadata().getStart();
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
			return this.getElements().getLast().getMetadata().getStart();
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
			buf.append(element).append(" ");
		}
		return buf.toString();
	}

	@Override
	public DefaultTISweepArea<T> clone() {
		return new DefaultTISweepArea<T>(this);
	}

	/**
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITemporalSweepArea#purgeElementsBefore(PointInTime)
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
		return getElements().peekLast();
	}
}
