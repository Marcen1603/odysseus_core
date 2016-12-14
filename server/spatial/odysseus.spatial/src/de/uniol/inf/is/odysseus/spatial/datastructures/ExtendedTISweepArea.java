package de.uniol.inf.is.odysseus.spatial.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;

public class ExtendedTISweepArea<T extends IStreamObject<? extends ITimeInterval>> extends DefaultTISweepArea<T> {

	private static final long serialVersionUID = -3472731370495830967L;

	transient Comparator<T> purgeComparator = new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			if (TimeInterval.totallyBefore(o1.getMetadata(), o2.getMetadata())) {
				return -1;
			}
			return 1;

		}
	};

	public List<T> queryAllElementsAsList() {
		List<T> result = new ArrayList<T>(getElements());
		return result;

	}

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

	@SuppressWarnings("unchecked")
	@Override
	public List<T> extractElementsBeforeAsList(PointInTime time) {

		List<T> removedElements = null;

		Tuple<ITimeInterval> tuple = new Tuple<ITimeInterval>();
		tuple.setMetadata(new TimeInterval(time, time));

		synchronized (getElements()) {

			if (getElements().size() > 10 && hasEndTsOrder) {
				removedElements = binarySearchPurge((T) tuple);
			} else {
				removedElements = iterativePurge((T) tuple);
			}
		}

		return removedElements;
	}

	private List<T> binarySearchPurge(T tuple) {

		// binary search returns the index of the search key, if it is
		// contained in the list;
		// otherwise, (-(insertion point) - 1). The insertion point is
		// defined as the point at which the key would be inserted into
		// the list: the index of the first element greater than the
		// key, or list.size() if all elements in the list are less than
		// the specified key. Note that this guarantees that the return
		// value will be >= 0 if and only if the key is found.
		// Remark: purgeComparator will never return equals!

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
						// before, hence, we have to go a step further
						delTo++;
					}

					// Get a view of the elements which are going to be removed
					List<T> subListView = getElements().subList(0, delTo - 1);

					// Copy this list so that we can return it
					List<T> removedElements = new ArrayList<>(subListView);

					// Remove the elements from the original list
					subListView.clear();

					// return the list with the (copied) removed elements
					return removedElements;

				} else {
					// We can remove everything
					getElements().clear();
				}
			}
		}
		// We can return an empty list
		return new ArrayList<>();
	}

	private List<T> iterativePurge(T tuple) {
		List<T> removedElements = new ArrayList<>(getElements().size());
		Iterator<T> it = this.getElements().iterator();

		while (it.hasNext()) {
			T cur = it.next();
			if (getRemovePredicate().evaluate(cur, tuple)) {
				removedElements.add(cur);
				it.remove();
			}

			if (cur.getMetadata().getStart().afterOrEquals(tuple.getMetadata().getStart())) {
				return removedElements;
			}
		}

		return removedElements;
	}

}
