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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.predicate.TotallyBeforePredicate;
import de.uniol.inf.is.odysseus.sweeparea.AbstractSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.FastArrayList;
import de.uniol.inf.is.odysseus.sweeparea.IFastList;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * This sweeparea implementation provides some optimizations on extract and
 * purge. The elements are always sorted and extract and purge only remove
 * elements, until the remove predicate first returns false. The remove
 * predicate is fixed to the TotallyBeforePredicate
 */
abstract public class AbstractTISweepArea<T extends IStreamObject<? extends ITimeInterval>> extends AbstractSweepArea<T>
		implements Comparable<AbstractTISweepArea<T>>, ITimeIntervalSweepArea<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractTISweepArea.class);

	private static final long serialVersionUID = 3380347798012193584L;

	protected T lastInserted = null;

	/**
	 * <p>
	 * This is <code>true</code> iff the end timestamp of the data stream
	 * elements in this sweep area are in ascending order. We need this
	 * information to decide if we can use the binary search in
	 * {@link AbstractTimeIntervalSweepArea#extractElements(IStreamObject, de.uniol.inf.is.odysseus.core.Order)}
	 * (e.g., in DefaultTISweepArea) or similar methods.
	 *
	 * <p>
	 * XXX: This flag is set to <code>true</code> only if the end timestamp has
	 * an increasing order (with equal values allowed). For the binary search it
	 * is necessary to ensure that the resulting element of the binary search is
	 * the last invalid element in the list. Therefore a linear search is done
	 * for the next element(s) after the index found by the binary index.
	 */
	protected boolean hasEndTsOrder = false;

	transient Comparator<T> purgeComparator = new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			if (TimeInterval.totallyBefore(o1.getMetadata(), o2.getMetadata())) {
				return -1;
			}
			return 1;

		}
	};

	// Must be public for service --> do not call directly
	public AbstractTISweepArea() {
		this(new FastArrayList<T>());
	}

	protected AbstractTISweepArea(IFastList<T> list) {
		super(list, new MetadataComparator<ITimeInterval>());
		super.setRemovePredicate(TotallyBeforePredicate.getInstance());
	}

	protected AbstractTISweepArea(AbstractTISweepArea<T> defaultTISweepArea)
			throws InstantiationException, IllegalAccessException {
		super(defaultTISweepArea);
	}

	// ---------------------------------------------------------------------------------------
	// Implementations from ISweepArea
	// ---------------------------------------------------------------------------------------

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

			if (getElements().size() > 5 && hasEndTsOrder) {
				// The correct order is given so that we can use binary search,
				// which is O(log(n))
				binarySearch(element, false);
			} else {
				// In case that the end-timestamps are not ordered we have to do
				// a linear iterative search, which is O(n)
				iterativeSearch(element, false);
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
	 * Removes the elements in an binary search approach.
	 *
	 * @param tuple
	 *            The tuple to compare the elements with. Used for the
	 *            timestamps.
	 * @param extract
	 *            If set to true, it will return a list of the extracted
	 *            elements. If set to false, it will just purge them and return
	 *            null. False may be a bit faster.
	 * @return A list of the removed elements or null, depending on the boolean
	 *         parameter
	 */
	private List<T> binarySearch(T tuple, final boolean extract) {

		/*
		 * binary search returns the index of the search key, if it is contained
		 * in the list; otherwise, (-(insertion point) - 1). The insertion point
		 * is defined as the point at which the key would be inserted into the
		 * list: the index of the first element greater than the key, or
		 * list.size() if all elements in the list are less than the specified
		 * key. Note that this guarantees that the return value will be >= 0 if
		 * and only if the key is found. Remark: purgeComparator will never
		 * return equals!
		 */
		int delTo = Collections.binarySearch(getElements(), (T) tuple, purgeComparator);
		// Only remove if before
		if (delTo < 0) {
			// find correct position
			delTo = (delTo + 1) * -1;
			if (delTo > 0) { // else there is nothing to do
				if (delTo < getElements().size()) {

					// Maybe there are more elements with the same
					// end-timestamp later in the list
					while (purgeComparator.compare(getElements().get(delTo), (T) tuple) == -1) {
						// There is another element which is totally
						// before. We have to go a step further
						delTo++;
					}

					// Get a view of the elements which are going to be removed
					List<T> subListView = getElements().subList(0, delTo);

					List<T> removedElements = null;
					if (extract) {
						// Copy this list so that we can return it
						removedElements = new ArrayList<>(subListView);
					}

					// Remove the elements from the original list
					subListView.clear();

					// return the list with the (copied) removed elements
					return removedElements;

				} else {
					// We can remove everything
					List<T> removedElements = null;
					if (extract) {
						removedElements = new ArrayList<>(getElements());
					}
					getElements().clear();
					return removedElements;
				}
			}
		}
		// We can return an empty list
		if (extract) {
			return new ArrayList<>();
		}
		return null;

	}

	/**
	 * Removes the elements in an iterative approach.
	 *
	 * @param tuple
	 *            The tuple to compare the elements with. Used for the
	 *            timestamps.
	 * @param extract
	 *            If set to true, it will return a list of the extracted
	 *            elements. If set to false, it will just purge them and return
	 *            null. False may be a bit faster.
	 * @return A list of the removed elements or null, depending on the boolean
	 *         parameter
	 */
	private List<T> iterativeSearch(T tuple, final boolean extract) {
		List<T> removedElements = null;
		if (extract) {
			removedElements = new ArrayList<>(getElements().size());
		}
		Iterator<T> it = this.getElements().iterator();

		while (it.hasNext()) {
			T cur = it.next();
			if (getRemovePredicate().evaluate(cur, tuple)) {
				if (extract) {
					// We only need to collect the tuples if we want to extract
					// them
					removedElements.add(cur);
				}
				it.remove();
			}

			if (cur.getMetadata().getStart().afterOrEquals(tuple.getMetadata().getStart())) {
				// We can end the loop cause the start-timestamp is ordered
				return removedElements;
			}
		}

		return removedElements;
	}

	// ---------------------------------------------------------------------------------------
	// Implementations from ITimeIntervalSweepArea
	// ----------------------------------------------------------------------------------------

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

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		return extractElementsBeforeAsList(time).iterator();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> extractElementsBeforeAsList(PointInTime time) {

		List<T> removedElements = null;

		Tuple<ITimeInterval> tuple = new Tuple<ITimeInterval>();
		tuple.setMetadata(new TimeInterval(time, time.plus(1)));

		synchronized (getElements()) {

			if (getElements().size() > 10 && hasEndTsOrder) {
				removedElements = binarySearch((T) tuple, true);
			} else {
				removedElements = iterativeSearch((T) tuple, true);
			}
		}

		return removedElements;
	}

	@Override
	public T peekLast() {
		return getElements().size() != 0 ? getElements().get(getElements().size() - 1) : null;
	}

	@Override
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

	@Override
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

	@Override
	public Iterator<T> extractElementsStartingBeforeOrEquals(PointInTime validity) {
		ArrayList<T> retval = new ArrayList<>();
		synchronized (getElements()) {
			Iterator<T> li = getElements().iterator();
			while (li.hasNext()) {
				T s_hat = li.next();
				if (s_hat.getMetadata().getStart().beforeOrEquals(validity)) {
					retval.add(s_hat);
					li.remove();
				} else {
					break;
				}
			}
		}
		return retval.iterator();
	}

	@Override
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

	@Override
	public Iterator<T> queryOverlaps(ITimeInterval t) {
		return queryOverlapsAsList(t).iterator();
	}

	@Override
	public List<T> queryContains(PointInTime point) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			for (T s : getElements()) {
				if (((TimeInterval) s.getMetadata()).includes(point)) {
					retval.add(s);
				}
			}
		}
		return retval;
	}

	@Override
	public Iterator<T> extractOverlaps(ITimeInterval t) {
		return extractOverlapsAsList(t).iterator();
	}

	@Override
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

	@Override
	public List<T> queryOverlapsAsListExtractOutdated(ITimeInterval interval, List<T> outdated) {
		ArrayList<T> retval = new ArrayList<T>();
		synchronized (getElements()) {
			Iterator<T> iter = getElements().iterator();
			while (iter.hasNext()) {
				T s = iter.next();
				if (TimeInterval.overlaps(s.getMetadata(), interval)) {
					retval.add(s);
				} else if (s.getMetadata().getEnd().before(interval.getStart())) {
					outdated.add(s);
					iter.remove();
				}
				// TODO: Find a better solution to state that area is timestamp
				// ordered!
				if (comparator != null) {
					if (s.getMetadata().getStart().after(interval.getEnd())) {
						break;
					}
				}

			}
		}
		return retval;
	}

	@Override
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

	/**
	 *
	 * @return the max start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the last element in the
	 *         linked list.
	 */
	@Override
	public PointInTime getMaxStartTs() {
		if (!this.getElements().isEmpty()) {
			return this.getElements().get(getElements().size() - 1).getMetadata().getStart();
		}
		return null;
	}

	/**
	 * @return the min start timestamp of all elements currently in the sweep
	 *         area. Should be the start timestamp of the first element in the
	 *         linked list.
	 */
	@Override
	public PointInTime getMinStartTs() {
		synchronized (getElements()) {
			if (!this.getElements().isEmpty()) {
				return this.getElements().get(0).getMetadata().getStart();
			}
		}
		return null;
	}

	@Override
	public int compareTo(AbstractTISweepArea<T> other) {
		if (this.getMinStartTs().before(other.getMinStartTs())) {
			return -1;
		} else if (this.getMinStartTs().after(other.getMinStartTs())) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public String getSweepAreaAsString(String tab, int max, boolean tail) {
		StringBuffer buf = new StringBuffer(tab);
		if (!tail) {
			for (int i = 0; i < Math.min(max, getElements().size()); i++) {
				T element = getElements().get(i);
				buf.append(" " + element).append(" ").append("\n" + tab);
			}
		} else {
			for (int i = Math.min(max, getElements().size()) - 1; i >= 0; i--) {
				T element = getElements().get(i);
				buf.append(" " + element).append(" ").append("\n" + tab);
			}
		}
		return buf.toString();
	}

	public String getSweepAreaAsString(String tab) {
		return getSweepAreaAsString(tab, getElements().size(), false);
	}

	public String getSweepAreaAsString() {
		return getSweepAreaAsString("");
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

	@Override
	public void insert(T element) {
		if (this.comparator == null || this.getElements().size() == 0
				|| this.comparator.compare(this.getElements().get(this.getElements().size() - 1), element) <= 0) {
			// add the element to the end of the list

			hasEndTsOrder = isStillInEndTsOrderAfterInsert(element, this.getElements().size());
			setLatestTimeStamp(element);
			this.getElements().add(element);
		} else {
			// find the position where the element should be inserted
			int pos = findInsertionPoint(element);
			hasEndTsOrder = isStillInEndTsOrderAfterInsert(element, pos);
			setLatestTimeStamp(element);
			this.getElements().add(pos, element);
		}
	}

	private int findInsertionPoint(T element) {
		int pos = Collections.binarySearch(this.getElements(), element, this.comparator);
		pos = subfind(element, pos);

		return pos;
	}

	private int subfind(T element, int pos) {
		if (pos >= 0) {
			// there is one ore more elements with the same ordering key

			// find the position of the last element with the same ordering
			// key
			while (this.comparator.compare(this.getElements().get(pos), element) == 0) {
				++pos;
			}
		} else {
			pos = (-(pos) - 1);
		}
		return pos;
	}

	/**
	 * <p>
	 * Checks if the end timestamp order is given after insertion of
	 * <code>element</code> at pos <code>pos</code>.
	 *
	 * <p>
	 * If the list of elements is empty, this method returns always true.
	 *
	 * <p>
	 * If the elements are already unordered (hasEndTsOrder is false and the
	 * list of elements is not empty), this method returns false.
	 *
	 * @param element
	 *            The element to insert.
	 * @param pos
	 *            The position where the element should be inserted.
	 * @return True, if the ordering of the end timestamp is still given after
	 *         the insert, falser otherwise.
	 */
	protected boolean isStillInEndTsOrderAfterInsert(T element, int pos) {
		if (this.getElements().size() == 0) {
			// If there are no elements in this sweep area, the end ts order is
			// obviously not false. So, we give it a new chance.
			return true;
		}
		if (!hasEndTsOrder) {
			// If if previously was false it won't be better with the new
			// element
			return false;
		}
		if (pos == 0) {
			// insertion at start of the list
			if (getElements().size() > 1) {
				PointInTime nextEndTs = this.getElements().get(1).getMetadata().getEnd();
				return nextEndTs.afterOrEquals(element.getMetadata().getEnd());
			} else {
				// First case from above: list with only one element is sorted
				return true;
			}
		} else if (pos == this.getElements().size()) {
			// insertion at the end of the list
			PointInTime prevEndTs = this.getElements().get(this.getElements().size() - 1).getMetadata().getEnd();
			return prevEndTs.beforeOrEquals(element.getMetadata().getEnd());
		} else {
			// Insertion in the middle of the list
			PointInTime prevEndTs = this.getElements().get(pos - 1).getMetadata().getEnd();
			PointInTime nextEndTs = this.getElements().get(pos).getMetadata().getEnd();
			return nextEndTs.afterOrEquals(element.getMetadata().getEnd())
					&& prevEndTs.beforeOrEquals(element.getMetadata().getEnd());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.AbstractSweepArea#insertAll(java.util.
	 * List)
	 */
	@Override
	public void insertAll(List<T> toBeInserted) {
		// XXX: check if it is ordered by end ts?
		// otherwise we could not make sure that it is ordered by end ts,
		// therefore:
		hasEndTsOrder = false;
		super.insertAll(toBeInserted);
	}

	/**
	 * Set the latest end timestamp for this sweeparea. Should happen on insert.
	 *
	 * @param current
	 *            end timestamp of the currently inserted element.
	 */
	protected void setLatestTimeStamp(T current) {
		if (this.lastInserted == null
				|| current.getMetadata().getEnd().after(this.lastInserted.getMetadata().getEnd())) {
			this.lastInserted = current;
		}
	}

	@Override
	public PointInTime getMaxEndTs() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Latest tuple: {}", this.lastInserted);
		}
		return (this.lastInserted != null ? this.lastInserted.getMetadata().getEnd() : null);
	}

	@Override
	public String getName() {
		return this.getClass().getName();
	}

	public boolean hasEndTsOrder() {
		return hasEndTsOrder;
	}

	@Override
	public Iterator<T> extractAllElements() {
		return extractAllElementsAsList().iterator();
	}

	public List<T> queryAllElementsAsList() {
		List<T> result = new ArrayList<T>(getElements());
		return result;

	}

	@Override
	public List<T> extractAllElementsAsList() {
		LinkedList<T> result = new LinkedList<T>();
		synchronized (getElements()) {
			Iterator<T> it = this.getElements().iterator();
			while (it.hasNext()) {
				T next = it.next();
				it.remove();
				result.add(next);
			}
		}
		return result;
	}

	@Override
	abstract public ITimeIntervalSweepArea<T> clone();

}
