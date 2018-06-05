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
package de.uniol.inf.is.odysseus.persistentqueries;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.server.intervalapproach.comparator.EndTsComparator;
import de.uniol.inf.is.odysseus.server.intervalapproach.comparator.StartTsComparator;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * This sweep area is used for equi joins over non-windowed streams. Instead of
 * a list, it uses a multi attribute hash map
 *
 * @author Andre Bolles, Cornelius Ludmann, Marco Grawunder
 *
 */
public class HashJoinSweepArea implements ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> {

	private static final long serialVersionUID = -8331551296317426445L;

	/**
	 * Store elements and access them by key
	 */
	private Map<Object, Set<Tuple<? extends ITimeInterval>>> elements;

	static protected final EndTsComparator endTsComp = new EndTsComparator();
	static protected final StartTsComparator startTsComp = new StartTsComparator();

	/**
	 * Access elements by start time index
	 */
	final private PriorityQueue<StreamObjectWrapper<Tuple<ITimeInterval>, ITimeInterval>> startTimeIndex;
	private PointInTime maxStartTs;

	/**
	 * Access elements by end time index
	 */
	final private PriorityQueue<StreamObjectWrapper<Tuple<ITimeInterval>, ITimeInterval>> endTimeIndex;
	private PointInTime maxEndTs;

	/**
	 * This list is used for projecting new elements that are to be inserted
	 * into this sweep area to the attributes of equi join predicate
	 */
	int[] insertRestrictList;

	/**
	 * This list is use for projecting elements from the other side to the
	 * attributes of the equi join predicate. These attributes can have other
	 * indices in their schema than the attributes used to insert elements into
	 * this sweepArea. However, the values of the attributes can be equal and
	 * will then lead to a join of elements.
	 */
	int[] queryRestrictList;

	private boolean inputOrderedByTime;

	private String areaName;

	@Override
	public ISweepArea<Tuple<? extends ITimeInterval>> newInstance(OptionMap options) {
		return new HashJoinSweepArea(this.insertRestrictList, this.queryRestrictList, this.inputOrderedByTime);
	}

	public HashJoinSweepArea(int[] insertRestrictList, int[] queryRestrictList, boolean inputOrderedByTime) {
		this.elements = new HashMap<>();
		this.insertRestrictList = insertRestrictList;
		check(insertRestrictList);
		this.queryRestrictList = queryRestrictList;
		this.inputOrderedByTime = inputOrderedByTime;
		if (inputOrderedByTime) {
			this.startTimeIndex = new PriorityQueue<>();
		} else {
			this.startTimeIndex = new PriorityQueue<>(11, startTsComp);
		}
		endTimeIndex = new PriorityQueue<>(11, endTsComp);
	}

	private void check(int[] insertRestrictList) {
		for (int v : insertRestrictList) {
			if (v == -1) {
				throw new IllegalArgumentException("Restrictlist cannot not contain -1");
			}
		}
	}

	public HashJoinSweepArea(HashJoinSweepArea original) {
		this.elements = original.elements;
		this.insertRestrictList = original.insertRestrictList;
		check(original.insertRestrictList);
		this.queryRestrictList = original.queryRestrictList;
		this.inputOrderedByTime = original.inputOrderedByTime;
		if (inputOrderedByTime) {
			this.startTimeIndex = new PriorityQueue<>();
		} else {
			this.startTimeIndex = new PriorityQueue<>(11, startTsComp);
		}
		this.startTimeIndex.addAll(original.startTimeIndex);
		endTimeIndex = new PriorityQueue<>(11, endTsComp);
		this.endTimeIndex.addAll(original.endTimeIndex);
	}

	protected static Object getKey(Tuple<? extends ITimeInterval> element, int[] restrictList) {
		if (restrictList.length == 1) {
			// TODO: save copy?
			return element.getAttribute(restrictList[0]);
		} else {
			return element.restrict(restrictList, true);
		}
	}

	@Override
	public void insert(Tuple<? extends ITimeInterval> element) {
		Object key = getKey(element, insertRestrictList);
		synchronized (this.elements) {
			putToElements(key, element);
			addToTimeIndex(element);
		}

	}

	private void putToElements(Object key, Tuple<? extends ITimeInterval> element) {
		Set<Tuple<? extends ITimeInterval>> set = this.elements.get(key);
		if (set == null) {
			set = Sets.newIdentityHashSet();
			this.elements.put(key, set);
		}
		set.add(element);
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryCopy(Tuple<? extends ITimeInterval> element, Order order,
			boolean extract) {
		if (this.elements.isEmpty()) {
			return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
		}

		List<Tuple<? extends ITimeInterval>> result;
		synchronized (this.elements) {
			Object key = getKey(element, queryRestrictList);
			if (!this.elements.containsKey(key))
				return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();

			result = new ArrayList<>();
			Collection<Tuple<? extends ITimeInterval>> matchingTuples = this.elements.get(key);

			for (Iterator<Tuple<? extends ITimeInterval>> iter = matchingTuples.iterator(); iter.hasNext();) {
				Tuple<? extends ITimeInterval> matchingTuple = iter.next();
				if (TimeInterval.overlaps(matchingTuple.getMetadata(), element.getMetadata())) {
					if (extract) {
						result.add(matchingTuple);
						removeFromTimeIndex(matchingTuple);
						iter.remove();
					} else {
						result.add(matchingTuple.clone());
					}
				}

			}
		}

		Collections.sort(result, startTsComp);
		return result.iterator();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void addToTimeIndex(Tuple<? extends ITimeInterval> element) {
		// TODO: is the initial setting of max usefull?
		// update maxTS-Values
		if (maxStartTs != null) {
			PointInTime p = element.getMetadata().getStart();
			if (maxStartTs.before(p)) {
				maxStartTs = p;
			}
		} else if (startTimeIndex.isEmpty()) {
			maxStartTs = element.getMetadata().getStart();
		}

		if (maxEndTs != null) {
			PointInTime p = element.getMetadata().getEnd();
			if (maxEndTs.before(p)) {
				maxEndTs = p;
			}
		} else if (endTimeIndex.isEmpty()) {
			maxEndTs = element.getMetadata().getEnd();
		}

		this.startTimeIndex.add(new StreamObjectWrapper(element));
		if (element.getMetadata().getEnd() != PointInTime.INFINITY) {
			this.endTimeIndex.add(new StreamObjectWrapper(element));
		}
	}

	private void removeFromTimeIndex(Tuple<? extends ITimeInterval> tuple) {
		// if bigger element is removed, then max values needs to be reset!
		// find new value on request
		if (tuple.getMetadata().getStart().after(maxStartTs)) {
			maxStartTs = null;
		}
		if (tuple.getMetadata().getEnd().after(maxEndTs)) {
			maxEndTs = null;
		}
		if (tuple.getMetadata().getEnd() != PointInTime.INFINITY) {
			startTimeIndex.remove(new StreamObjectWrapper<>(tuple));
			endTimeIndex.remove(new StreamObjectWrapper<>(tuple));
		}
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElements(Tuple<? extends ITimeInterval> element,
			Order order) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void purgeElements(Tuple<? extends ITimeInterval> element, Order order) {
		purgeElementsBefore(element.getMetadata().getStart());
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertAll(List<Tuple<? extends ITimeInterval>> toBeInserted) {
		for (Tuple<? extends ITimeInterval> r : toBeInserted) {
			this.insert(r);
		}
	}

	@Override
	public void clear() {
		this.elements.clear();
		this.startTimeIndex.clear();
		this.endTimeIndex.clear();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> query(Tuple<? extends ITimeInterval> element,
			de.uniol.inf.is.odysseus.core.Order order) {
		if (this.elements.isEmpty()) {
			return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
		}

		synchronized (this.elements) {
			Object key = getKey(element, queryRestrictList);
			if (!this.elements.containsKey(key))
				return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();

			Collection<Tuple<? extends ITimeInterval>> matchingTuples = this.elements.get(key);
			// TODO: check time overlap
			return matchingTuples.iterator();
		}
	}

	@Override
	public Tuple<? extends ITimeInterval> peek() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Tuple<? extends ITimeInterval> poll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Tuple<? extends ITimeInterval> element) {
		Object key = getKey(element, insertRestrictList);
		synchronized (this.elements) {
			removeFromTimeIndex(element);
			return removeFromElements(key, element);
		}
	}

	/**
	 * @param key
	 * @param element
	 * @return
	 */
	private boolean removeFromElements(Object key, Tuple<? extends ITimeInterval> element) {
		Set<Tuple<? extends ITimeInterval>> set = this.elements.get(key);
		if (set == null) {
			return false;
		}
		boolean result = set.remove(element);
		if (set.isEmpty()) {
			this.elements.remove(key);
		}
		return result;
	}

	@Override
	public void removeAll(List<Tuple<? extends ITimeInterval>> toBeRemoved) {
		// TODO: Why not clear?
		for (Tuple<? extends ITimeInterval> t : toBeRemoved) {
			this.remove(t);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public IPredicate<? super Tuple<? extends ITimeInterval>> getQueryPredicate() {
		return null;
	}

	@Override
	public void setQueryPredicate(IPredicate<? super Tuple<? extends ITimeInterval>> queryPredicate) {
	}

	@Override
	public void setRemovePredicate(IPredicate<? super Tuple<? extends ITimeInterval>> removePredicate) {
	}

	@Override
	public IPredicate<? super Tuple<? extends ITimeInterval>> getRemovePredicate() {
		return null;
	}

	@Override
	public HashJoinSweepArea clone() {
		return new HashJoinSweepArea(this);
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		// Attention: Priority Queue iterator does not retrieve values in
		// sort order
		StreamObjectWrapper<Tuple<ITimeInterval>, ITimeInterval> top = endTimeIndex.peek();
		while (top != null && top.getMetadata().getEnd().beforeOrEquals(time)) {
			endTimeIndex.poll();
			Object key = getKey(top.getWrappedObject(), insertRestrictList);
			synchronized (this.elements) {
				removeFromElements(key, top.getWrappedObject());
				startTimeIndex.remove(top);
			}
			top = endTimeIndex.peek();
		}
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsBefore(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> extractElementsBeforeAsList(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public PointInTime getMinStartTs() {
		if (startTimeIndex.isEmpty()){
			return null;
		}
		return startTimeIndex.peek().getMetadata().getStart();
	}

	@Override
	public PointInTime getMaxStartTs() {
		if (maxStartTs == null) {
			// Priority queue does not give elements in any order
			// so iterate over queue and determine max element is necessary
			Iterator<StreamObjectWrapper<Tuple<ITimeInterval>, ITimeInterval>> iter = startTimeIndex.iterator();
			PointInTime currentMax = null;
			if (iter.hasNext()) {
				currentMax = iter.next().getMetadata().getStart();
				while (iter.hasNext()) {
					PointInTime toTest = iter.next().getMetadata().getStart();
					if (toTest.after(currentMax)){
						currentMax = toTest;
					}
				}

			}
			maxStartTs = currentMax;
		}
		return maxStartTs;
	}


	@Override
	public PointInTime getMaxEndTs() {
		if (maxEndTs == null) {
			// Priority queue does not give elements in any order
			// so iterate over queue and determine max element is necessary
			Iterator<StreamObjectWrapper<Tuple<ITimeInterval>, ITimeInterval>> iter = endTimeIndex.iterator();
			PointInTime currentMax = null;
			if (iter.hasNext()) {
				currentMax = iter.next().getMetadata().getEnd();
				while (iter.hasNext()) {
					PointInTime toTest = iter.next().getMetadata().getEnd();
					if (toTest.after(currentMax)){
						currentMax = toTest;
					}
				}

			}
			maxEndTs = currentMax;
		}
		return maxEndTs;
	}

	@Override
	public String toString() {
		String s = "";
		for (int i : this.insertRestrictList) {
			s += i + " ";
		}

		s += " ||| ";

		for (int i : this.queryRestrictList) {
			s += i + " ";
		}

		return s;
	}



	@Override
	public String getName() {
		return "HashJoinSA";
	}

	@Override
	public Tuple<? extends ITimeInterval> peekLast() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractAllElements() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> extractAllElementsAsList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingBefore(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryElementsStartingBefore(PointInTime validity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingBeforeOrEquals(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingEquals(PointInTime validity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> queryOverlapsAsList(ITimeInterval interval) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryOverlaps(ITimeInterval interval) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> queryContains(PointInTime point) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractOverlaps(ITimeInterval t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> extractOverlapsAsList(ITimeInterval t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSweepAreaAsString(String tab, int max, boolean tail) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> queryOverlapsAsListExtractOutdated(ITimeInterval interval,
			List<Tuple<? extends ITimeInterval>> outdated) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getAreaName() {
		return this.areaName;
	}

	@Override
	public void setAreaName(String name) {
		this.areaName = name;
	}
}