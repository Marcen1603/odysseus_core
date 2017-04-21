package de.uniol.inf.is.odysseus.intervalapproach.sweeparea;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;

import com.google.common.collect.TreeMultimap;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * Copyright 2015 The Odysseus Team
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

/**
 *
 * <p> This sweep area uses Trees to access tuples by start ts resp. end ts.
 *
 * <p>This implementation does not allow to equal elements (e1.equals(e2)) at the same start ts). MARK: Better use TreeMap<Long, List<T>)?
 *
 * @author Cornelius Ludmann
 */
public class TreeMapTISweepArea<T extends IStreamObject<? extends ITimeInterval>> implements ITimeIntervalSweepArea<T>, Serializable {
	private static final long serialVersionUID = 8050753498900975533L;

	private final TreeMultimap<Long, T> elements = TreeMultimap.create(Comparator.naturalOrder(), new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			return Comparator.<Long>naturalOrder().compare(o1.getMetadata().getStart().getMainPoint(), o1.getMetadata().getStart().getMainPoint());
		}
	});
	private final TreeMultimap<Long, T> elementsByEndTs = TreeMultimap.create(Comparator.naturalOrder(), new Comparator<T>() {

		@Override
		public int compare(T o1, T o2) {
			return Comparator.<Long>naturalOrder().compare(o1.getMetadata().getStart().getMainPoint(), o1.getMetadata().getStart().getMainPoint());
		}
	});

	@SuppressWarnings("unused")
	private transient IPredicate<? super T> queryPredicate;
	@SuppressWarnings("unused")
	private transient IPredicate<? super T> removePredicate;

	/**
	 *
	 */
	public TreeMapTISweepArea() {
		super();
	}

	/**
	 * @param treeMapTISweepArea
	 */
	public TreeMapTISweepArea(TreeMapTISweepArea<T> treeMapTISweepArea) {
		this.elements.putAll(treeMapTISweepArea.elements);
		this.elementsByEndTs.putAll(treeMapTISweepArea.elementsByEndTs);
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return elements.values().iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#init()
	 */
	@Override
	public void init() {
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#clear()
	 */
	@Override
	public void clear() {
		elements.clear();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#size()
	 */
	@Override
	public int size() {
		return elements.size();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#insert(java.lang.Object)
	 */
	@Override
	public void insert(T element) {
		synchronized (elements) {
			elements.put(element.getMetadata().getStart().getMainPoint(), element);
			elementsByEndTs.put(element.getMetadata().getEnd().getMainPoint(), element);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#insertAll(java.util.List)
	 */
	@Override
	public void insertAll(List<T> toBeInserted) {
		synchronized (elements) {
			for(T element : toBeInserted) {
				elements.put(element.getMetadata().getStart().getMainPoint(), element);
				elementsByEndTs.put(element.getMetadata().getEnd().getMainPoint(), element);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(T element) {
		synchronized (elements) {
			elements.remove(element.getMetadata().getStart().getMainPoint(), element);
			return elementsByEndTs.remove(element.getMetadata().getEnd().getMainPoint(), element);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#removeAll(java.util.List)
	 */
	@Override
	public void removeAll(List<T> toBeRemoved) {
		synchronized (elements) {
			for(T element : toBeRemoved) {
				elements.remove(element.getMetadata().getStart().getMainPoint(), element);
				elementsByEndTs.remove(element.getMetadata().getEnd().getMainPoint(), element);
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#peek()
	 */
	@Override
	public T peek() {
		return elements.get(elements.keySet().last()).last();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#poll()
	 */
	@Override
	public T poll() {
		synchronized (elements) {
			Long key = elements.keySet().last();
			T element = elements.get(key).last();
			if(remove(element)) {
				return element;
			} else {
				return null;
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#query(java.lang.Object, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<T> query(T element, Order order) {
		System.out.println("not implemented yet");
		throw new UnsupportedOperationException();
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#queryCopy(java.lang.Object, de.uniol.inf.is.odysseus.core.Order, boolean)
	 */
	@Override
	public Iterator<T> queryCopy(T element, Order order, boolean extract) {
		// TODO Auto-generated method stub
		System.out.println("not implemented yet");
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#extractElements(java.lang.Object, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<T> extractElements(T element, Order order) {
		// TODO Auto-generated method stub
		System.out.println("not implemented yet");
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#setQueryPredicate(de.uniol.inf.is.odysseus.core.predicate.IPredicate)
	 */
	@Override
	public void setQueryPredicate(IPredicate<? super T> queryPredicate) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getQueryPredicate()
	 */
	@Override
	public IPredicate<? super T> getQueryPredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#purgeElements(java.lang.Object, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public void purgeElements(T element, Order order) {
		// TODO Auto-generated method stub
		System.out.println("not implemented yet");
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#setRemovePredicate(de.uniol.inf.is.odysseus.core.predicate.IPredicate)
	 */
	@Override
	public void setRemovePredicate(IPredicate<? super T> removePredicate) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getRemovePredicate()
	 */
	@Override
	public IPredicate<? super T> getRemovePredicate() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#extractAllElements()
	 */
	@Override
	public List<T> extractAllElementsAsList() {
		LinkedList<T> result;
		synchronized (elements) {
			result = new LinkedList<T>(elements.values());
			elements.clear();
			elementsByEndTs.clear();
		}
		return result;
	}

	@Override
	public Iterator<T> extractAllElements() {
		return extractAllElementsAsList().iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#clone()
	 */
	@Override
	public TreeMapTISweepArea<T> clone() {
		return new TreeMapTISweepArea<T>(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getName()
	 */
	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#newInstance(de.uniol.inf.is.odysseus.core.collection.OptionMap)
	 */
	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#purgeElementsBefore(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public void purgeElementsBefore(PointInTime time) {
		extractElementsBefore(time);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractElementsBefore(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public List<T> extractElementsBeforeAsList(PointInTime time) {
		List<T> result = new LinkedList<>();
		synchronized (elements) {
			NavigableSet<Long> keys = elementsByEndTs.keySet().headSet(time.getMainPoint(), true);
			for(Long key : keys) {
				result.addAll(elementsByEndTs.get(key));
			}
		}
		removeAll(result);
		return result;
	}

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		return extractAllElementsAsList().iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#peekLast()
	 */
	@Override
	public T peekLast() {
		// TODO Auto-generated method stub
		System.out.println("not implemented yet");
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractElementsStartingBefore(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Iterator<T> extractElementsStartingBefore(PointInTime time) {
		List<T> result = new LinkedList<>();
		synchronized (elements) {
			NavigableSet<Long> keys = elements.keySet().headSet(time.getMainPoint(), false);
			for(Long key : keys) {
				result.addAll(elements.get(key));
			}
		}
		removeAll(result);
		return result.iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#queryElementsStartingBefore(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Iterator<T> queryElementsStartingBefore(PointInTime time) {
		List<T> result = new LinkedList<>();
		synchronized (elements) {
			NavigableSet<Long> keys = elements.keySet().headSet(time.getMainPoint(), false);
			for(Long key : keys) {
				result.addAll(elements.get(key));
			}
		}
		return result.iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractElementsStartingBeforeOrEquals(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Iterator<T> extractElementsStartingBeforeOrEquals(PointInTime time) {
		List<T> result = new LinkedList<>();
		synchronized (elements) {
			NavigableSet<Long> keys = elements.keySet().headSet(time.getMainPoint(), true);
			for(Long key : keys) {
				result.addAll(elements.get(key));
			}
		}
		removeAll(result);
		return result.iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractElementsStartingEquals(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Iterator<T> extractElementsStartingEquals(PointInTime validity) {
		synchronized (elements) {
			return elements.get(validity.getMainPoint()).iterator();
		}
	}


	private List<T> overlaps(ITimeInterval interval, boolean remove) {
		synchronized (elements) {

			// all elements that start before interval ends:
			SortedSet<Long> startBeforeIntervalEnds = elements.keySet().headSet(interval.getEnd().getMainPoint());

			// all elements that end after interval starts:
			SortedSet<Long> endAfterIntervalStarts = elementsByEndTs.keySet().tailSet(interval.getStart().getMainPoint());

			List<T> startBeforeIntervalEndsSet = new LinkedList<>();
			for(Long ts : startBeforeIntervalEnds) {
				startBeforeIntervalEndsSet.addAll(elements.get(ts));
			}

			List<T> endAfterIntervalStartsSet = new LinkedList<>();
			for(Long ts : endAfterIntervalStarts) {
				endAfterIntervalStartsSet.addAll(elementsByEndTs.get(ts));
			}

			startBeforeIntervalEndsSet.retainAll(endAfterIntervalStartsSet);

			if(remove) {
				removeAll(startBeforeIntervalEndsSet);
			}

			return startBeforeIntervalEndsSet;
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#queryOverlaps(de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public Iterator<T> queryOverlaps(ITimeInterval interval) {
		return overlaps(interval, false).iterator();
	}

	@Override
	public List<T> queryContains(PointInTime point) {
		// TODO: IMPLEMENT ME!
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractOverlaps(de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public Iterator<T> extractOverlaps(ITimeInterval interval) {
		return overlaps(interval, true).iterator();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#queryOverlapsAsList(de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public List<T> queryOverlapsAsList(ITimeInterval interval) {
		return overlaps(interval, false);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractOverlapsAsList(de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public List<T> extractOverlapsAsList(ITimeInterval interval) {
		return overlaps(interval, true);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMaxTs()
	 */
	@Override
	public PointInTime getMaxStartTs() {
		if(elements.isEmpty())
			return null;
		return new PointInTime(elements.keySet().last());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMinTs()
	 */
	@Override
	public PointInTime getMinStartTs() {
		if(elements.isEmpty())
			return null;
		return new PointInTime(elements.keySet().first());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMaxEndTs()
	 */
	@Override
	public PointInTime getMaxEndTs() {
		if(elements.isEmpty())
			return null;
		return new PointInTime(elementsByEndTs.keySet().last());
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getSweepAreaAsString(java.lang.String, int, boolean)
	 */
	@Override
	public String getSweepAreaAsString(String tab, int max, boolean tail) {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public List<T> queryOverlapsAsListExtractOutdated(ITimeInterval interval, List<T> outdated) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAreaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAreaName(String name) {
		// TODO Auto-generated method stub

	}


}
