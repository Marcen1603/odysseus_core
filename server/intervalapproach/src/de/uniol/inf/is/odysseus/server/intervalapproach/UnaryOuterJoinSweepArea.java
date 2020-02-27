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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperatorKeyValueProvider;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * @author Cornelius Ludmann
 *
 */
public class UnaryOuterJoinSweepArea
		implements ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>>, IPhysicalOperatorKeyValueProvider {

	private static final long serialVersionUID = -8554818456477053324L;

	private final Logger LOG = LoggerFactory.getLogger(UnaryOuterJoinSweepArea.class);

	private final Map<Object, Tuple<? extends ITimeInterval>> elements = new HashMap<>();

	private final TreeMap<PointInTime, List<Tuple<? extends ITimeInterval>>> outdatingElements = new TreeMap<>();

	private long count = 0;

	private long sumTimediff;

	private PointInTime minTs = null;

	/**
	 * This list is used for projecting new elements that are to be inserted
	 * into this sweep area to the attributes of equi join predicate
	 */
	private final int[] insertRestrictList;

	/**
	 * This list is use for projecting elements from the other side to the
	 * attributes of the equi join predicate. These attributes can have other
	 * indices in their schema than the attributes used to insert elements into
	 * this sweepArea. However, the values of the attributes can be equal and
	 * will then lead to a join of elements.
	 */
	private final int[] queryRestrictList;

	private final boolean outputArbitraryIfNoMatch;

	private final Tuple<? extends ITimeInterval> emptyElement;

	private String areaName;

	/**
	 * 
	 */
	public UnaryOuterJoinSweepArea(int[] insertRestrictList, int[] queryRestrictList, boolean outputArbitraryIfNoMatch,
			Tuple<? extends ITimeInterval> emptyElement) {
		this.insertRestrictList = Arrays.copyOf(insertRestrictList, insertRestrictList.length);
		check(insertRestrictList);
		this.queryRestrictList = Arrays.copyOf(queryRestrictList, queryRestrictList.length);
		check(queryRestrictList);
		this.outputArbitraryIfNoMatch = outputArbitraryIfNoMatch;
		this.emptyElement = emptyElement.clone();
	}

	/**
	 * 
	 */
	public UnaryOuterJoinSweepArea(UnaryOuterJoinSweepArea other) {
		this.insertRestrictList = Arrays.copyOf(other.insertRestrictList, other.insertRestrictList.length);
		this.queryRestrictList = Arrays.copyOf(other.queryRestrictList, other.queryRestrictList.length);
		this.outputArbitraryIfNoMatch = other.outputArbitraryIfNoMatch;
		this.emptyElement = other.emptyElement.clone();
	}

	private static void check(int[] restrictList) {
		for (int v : restrictList) {
			if (v == -1) {
				throw new IllegalArgumentException("Restrictlist cannot not contain -1");
			}
		}
	}

	private static Object getKey(Tuple<? extends ITimeInterval> element, int[] restrictList) {
		if (restrictList.length == 1) {
			// TODO: save copy?
			return element.getAttribute(restrictList[0]);
		} else {
			return element.restrict(restrictList, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#init()
	 */
	@Override
	public void init() {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#clear()
	 */
	@Override
	public void clear() {
		this.elements.clear();
		this.outdatingElements.clear();
		this.count = 0;
		this.sumTimediff = 0;
		this.minTs = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return elements.isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#size()
	 */
	@Override
	public int size() {
		return elements.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#insert(java.lang.Object)
	 */
	@Override
	public void insert(Tuple<? extends ITimeInterval> element) {
		Object key = getKey(element, insertRestrictList);
		synchronized (this.elements) {
			Tuple<? extends ITimeInterval> prev = this.elements.put(key, element);
			if (prev != null && !prev.getMetadata().getEnd().isInfinite()) {
				removeFromOutdatingElements(prev);
			}
			if (!element.getMetadata().getEnd().isInfinite()) {
				putToOutdatingElements(element);
			}
		}
	}

	/**
	 * @param prev
	 */
	private void removeFromOutdatingElements(Tuple<? extends ITimeInterval> prev) {
		if (prev == null)
			return;
		List<Tuple<? extends ITimeInterval>> list = this.outdatingElements.get(prev.getMetadata().getEnd());
		if (list == null) {
			LOG.warn("there is no element in outdating elements that match", prev);
			return;
		}
		if (!list.remove(prev)) {
			LOG.warn("there is no element in outdating elements that match", prev);
		} else {
			if (list.isEmpty()) {
				this.outdatingElements.remove(prev.getMetadata().getEnd());
			}
		}
	}

	/**
	 * @param element
	 */
	private void putToOutdatingElements(Tuple<? extends ITimeInterval> element) {
		if (element == null)
			return;
		List<Tuple<? extends ITimeInterval>> list = this.outdatingElements.get(element.getMetadata().getEnd());
		if (list == null) {
			list = new ArrayList<>();
			this.outdatingElements.put(element.getMetadata().getEnd(), list);
		}
		list.add(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#insertAll(java.util.List)
	 */
	@Override
	public void insertAll(List<Tuple<? extends ITimeInterval>> toBeInserted) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Tuple<? extends ITimeInterval> element) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#removeAll(java.util.List)
	 */
	@Override
	public void removeAll(List<Tuple<? extends ITimeInterval>> toBeRemoved) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#peek()
	 */
	@Override
	public Tuple<? extends ITimeInterval> peek() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#poll()
	 */
	@Override
	public Tuple<? extends ITimeInterval> poll() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#query(java.lang.Object,
	 * de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> query(Tuple<? extends ITimeInterval> element, Order order) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#queryCopy(java.lang.Object,
	 * de.uniol.inf.is.odysseus.core.Order, boolean)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryCopy(Tuple<? extends ITimeInterval> element, Order order,
			boolean extract) {
		synchronized (this.elements) {
			Object key = getKey(element, queryRestrictList);
			Tuple<? extends ITimeInterval> result = elements.get(key);
			if (result == null) {
				if (outputArbitraryIfNoMatch && !elements.isEmpty()) {
					result = elements.values().iterator().next();
				} else {
					result = emptyElement;
				}
			}

			if (result != emptyElement) {
				result = result.clone();

				long timediff = element.getMetadata().getStart().getMainPoint()
						- result.getMetadata().getStart().getMainPoint();

				if (timediff < 0) {

					if (LOG.isWarnEnabled()) {
						LOG.warn("[{}] Time diff is < 0 - diff: {} - element metadata: {} - result metadata: {}",
								areaName, timediff, element.getMetadata(), result.getMetadata());
					}

					result.getMetadata().setStartAndEnd(element.getMetadata().getStart(),
							element.getMetadata().getEnd());

					timediff = 0;
				}
				++count;
				sumTimediff += timediff;
			} else {
				result = result.clone();
			}

			minTs = element.getMetadata().getStart();

			List<Tuple<? extends ITimeInterval>> list = Collections.singletonList(result);
			return list.iterator();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#extractElements(java.lang.
	 * Object, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElements(Tuple<? extends ITimeInterval> element,
			Order order) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#setQueryPredicate(de.uniol.
	 * inf.is.odysseus.core.predicate.IPredicate)
	 */
	@Override
	public void setQueryPredicate(IPredicate<? super Tuple<? extends ITimeInterval>> queryPredicate) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getQueryPredicate()
	 */
	@Override
	public IPredicate<? super Tuple<? extends ITimeInterval>> getQueryPredicate() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#purgeElements(java.lang.
	 * Object, de.uniol.inf.is.odysseus.core.Order)
	 */
	@Override
	public void purgeElements(Tuple<? extends ITimeInterval> element, Order order) {
		synchronized (elements) {
			NavigableMap<PointInTime, List<Tuple<? extends ITimeInterval>>> outdated = outdatingElements
					.headMap(element.getMetadata().getStart(), true);
			for (Iterator<Entry<PointInTime, List<Tuple<? extends ITimeInterval>>>> iter = outdated.entrySet()
					.iterator(); iter.hasNext();) {
				List<Tuple<? extends ITimeInterval>> list = iter.next().getValue();
				for (Tuple<? extends ITimeInterval> e : list) {
					if (elements.remove(getKey(e, insertRestrictList)) == null) {
						LOG.warn("there is no element '{}' in elements map", e);
					}
				}
				iter.remove();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#setRemovePredicate(de.uniol
	 * .inf.is.odysseus.core.predicate.IPredicate)
	 */
	@Override
	public void setRemovePredicate(IPredicate<? super Tuple<? extends ITimeInterval>> removePredicate) {
		// nothing to do
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getRemovePredicate()
	 */
	@Override
	public IPredicate<? super Tuple<? extends ITimeInterval>> getRemovePredicate() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#extractAllElements()
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractAllElements() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#extractAllElementsAsList()
	 */
	@Override
	public List<Tuple<? extends ITimeInterval>> extractAllElementsAsList() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getName()
	 */
	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ISweepArea#newInstance(de.uniol.inf.is
	 * .odysseus.core.collection.OptionMap)
	 */
	@Override
	public ISweepArea<Tuple<? extends ITimeInterval>> newInstance(OptionMap options) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> iterator() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * purgeElementsBefore(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public void purgeElementsBefore(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * extractElementsBefore(de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsBefore(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * extractElementsBeforeAsList(de.uniol.inf.is.odysseus.core.metadata.
	 * PointInTime)
	 */
	@Override
	public List<Tuple<? extends ITimeInterval>> extractElementsBeforeAsList(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#peekLast()
	 */
	@Override
	public Tuple<? extends ITimeInterval> peekLast() {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * extractElementsStartingBefore(de.uniol.inf.is.odysseus.core.metadata.
	 * PointInTime)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingBefore(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * queryElementsStartingBefore(de.uniol.inf.is.odysseus.core.metadata.
	 * PointInTime)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryElementsStartingBefore(PointInTime validity) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * extractElementsStartingBeforeOrEquals(de.uniol.inf.is.odysseus.core.
	 * metadata.PointInTime)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingBeforeOrEquals(PointInTime time) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * extractElementsStartingEquals(de.uniol.inf.is.odysseus.core.metadata.
	 * PointInTime)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingEquals(PointInTime validity) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#queryOverlaps(
	 * de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryOverlaps(ITimeInterval interval) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#extractOverlaps
	 * (de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractOverlaps(ITimeInterval t) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * queryOverlapsAsList(de.uniol.inf.is.odysseus.core.metadata.ITimeInterval)
	 */
	@Override
	public List<Tuple<? extends ITimeInterval>> queryOverlapsAsList(ITimeInterval interval) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * queryOverlapsAsListExtractOutdated(de.uniol.inf.is.odysseus.core.metadata
	 * .ITimeInterval, java.util.List)
	 */
	@Override
	public List<Tuple<? extends ITimeInterval>> queryOverlapsAsListExtractOutdated(ITimeInterval interval,
			List<Tuple<? extends ITimeInterval>> outdated) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * extractOverlapsAsList(de.uniol.inf.is.odysseus.core.metadata.
	 * ITimeInterval)
	 */
	@Override
	public List<Tuple<? extends ITimeInterval>> extractOverlapsAsList(ITimeInterval t) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#queryContains(
	 * de.uniol.inf.is.odysseus.core.metadata.PointInTime)
	 */
	@Override
	public List<Tuple<? extends ITimeInterval>> queryContains(PointInTime point) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMaxTs()
	 */
	@Override
	public PointInTime getMaxStartTs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMinTs()
	 */
	@Override
	public PointInTime getMinStartTs() {
		return minTs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMaxEndTs()
	 */
	@Override
	public PointInTime getMaxEndTs() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#clone()
	 */
	@Override
	public ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> clone() {
		return new UnaryOuterJoinSweepArea(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#
	 * getSweepAreaAsString(java.lang.String, int, boolean)
	 */
	@Override
	public String getSweepAreaAsString(String tab, int max, boolean tail) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.
	 * IPhysicalOperatorKeyValueProvider#getKeyValues()
	 */
	@Override
	public Map<String, String> getKeyValues() {
		Map<String, String> result = new HashMap<>();
		result.put("Elements size", "" + elements.size());
		String s = elements.keySet().toString();
		result.put("Elements keys", s.substring(0, Math.min(s.length(), 50)) + (s.length() > 50 ? " ..." : ""));
		result.put("Outdating points size", "" + outdatingElements.size());
		result.put("Count", "" + count);
		result.put("Sum timediff", "" + sumTimediff);
		if (count != 0) {
			result.put("Avg timediff", "" + (sumTimediff / count));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#setAreaName(java.lang.
	 * String)
	 */
	@Override
	public void setAreaName(String name) {
		this.areaName = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.sweeparea.ISweepArea#getAreaName()
	 */
	@Override
	public String getAreaName() {
		return areaName;
	}

}