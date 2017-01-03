package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;

public class ExtendedTISweepArea<T extends IStreamObject<? extends ITimeInterval>> extends DefaultTISweepArea<T> {

	private static final long serialVersionUID = -3472731370495830967L;
	public static final String NAME = "ExtendedTISweepArea";

	transient Comparator<T> purgeComparator = new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			if (TimeInterval.totallyBefore(o1.getMetadata(), o2.getMetadata())) {
				return -1;
			}
			return 1;

		}
	};

	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		return new ExtendedTISweepArea<T>();
	}

	public List<T> queryAllElementsAsList() {
		List<T> result = new ArrayList<T>(getElements());
		return result;

	}

	/*
	 * The difference to the default implementation is that windows (sweepareas)
	 * with equal end-timestamps are also considered as sorted wherefore the way
	 * faster binary search can be used in more cases. (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.intervalapproach.sweeparea.AbstractTISweepArea#
	 * isStillInEndTsOrderAfterInsert(de.uniol.inf.is.odysseus.core.metadata.
	 * IStreamObject, int)
	 */
	@Override
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
				binarySearch(element, false);
			} else {
				iterativeSearch(element, false);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> extractElementsBeforeAsList(PointInTime time) {

		List<T> removedElements = null;

		Tuple<ITimeInterval> tuple = new Tuple<ITimeInterval>();
		tuple.setMetadata(new TimeInterval(time.plus(1), time.plus(1)));

		synchronized (getElements()) {

			if (getElements().size() > 10 && hasEndTsOrder) {
				removedElements = binarySearch((T) tuple, true);
			} else {
				removedElements = iterativeSearch((T) tuple, true);
			}
		}

		return removedElements;
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

	// Copied from the JoinTISweepArea
	@Override
	public Iterator<T> queryCopy(T element, Order order, boolean extract) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> iter;
		synchronized (this.getElements()) {
			switch (order) {
			case LeftRight:
				iter = this.getElements().iterator();
				while (iter.hasNext()) {
					T next = iter.next();
					if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
						break;
					}
					if (getQueryPredicate().evaluate(element, next)) {
						result.add(next);
						if (extract) {
							iter.remove();
						}
					}

				}
				break;
			case RightLeft:
				iter = this.getElements().iterator();
				while (iter.hasNext()) {
					T next = iter.next();
					if (TimeInterval.totallyBefore(next.getMetadata(), element.getMetadata())) {
						continue;
					}
					if (TimeInterval.totallyAfter(next.getMetadata(), element.getMetadata())) {
						break;
					}
					if (getQueryPredicate().evaluate(next, element)) {
						result.add(next);
						if (extract) {
							iter.remove();
						}
					}
				}
				break;
			}
		}
		return result.iterator();
	}

	@Override
	public String getName() {
		return NAME;
	}
}
