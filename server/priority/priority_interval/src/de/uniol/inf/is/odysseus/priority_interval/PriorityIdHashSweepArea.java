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
package de.uniol.inf.is.odysseus.priority_interval;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.LinkedHashMultimap;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class PriorityIdHashSweepArea<K extends ITimeIntervalPriority, T extends Tuple<K>>
		implements ITimeIntervalSweepArea<T> {

	private static final long serialVersionUID = 7057369128980848365L;

	private LinkedHashMultimap<Long, T> elements = LinkedHashMultimap.create();
	final private int storedIdPosition;
	final private int externalIdPosition;

	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		throw new UnsupportedOperationException();
		// return null;
	}

	public PriorityIdHashSweepArea(int externalIdPosition, int storedIdPosition) {
		this.storedIdPosition = storedIdPosition;
		this.externalIdPosition = externalIdPosition;
	}

	public PriorityIdHashSweepArea(PriorityIdHashSweepArea<K, T> priorityIdHashSweepArea) {
		this.storedIdPosition = priorityIdHashSweepArea.storedIdPosition;
		this.externalIdPosition = priorityIdHashSweepArea.externalIdPosition;
		this.elements = LinkedHashMultimap.create(priorityIdHashSweepArea.elements);
	}

	@Override
	public List<T> extractElementsBeforeAsList(PointInTime time) {
		Iterator<T> it = elements.values().iterator();
		LinkedList<T> extractedElements = new LinkedList<T>();
		while (it.hasNext()) {
			T curElement = it.next();
			if (curElement.getMetadata().getEnd().before(time)) {
				it.remove();
				extractedElements.add(curElement);
			} else {
				if (curElement.getMetadata().getPriority() == 0) {
					break;
				}
			}
		}
		return extractedElements;
	}

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		return extractElementsBeforeAsList(time).iterator();
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		Iterator<T> it = elements.values().iterator();
		while (it.hasNext()) {
			T curElement = it.next();
			if (curElement.getMetadata().getEnd().beforeOrEquals(time)) {
				it.remove();
			} else {
				if (curElement.getMetadata().getPriority() == 0) {
					break;
				}
			}
		}
	}

	@Override
	public void clear() {
		this.elements.clear();
	}

	@Override
	public Iterator<T> extractElements(T element, de.uniol.inf.is.odysseus.core.Order order) {
		return extractElementsBefore(element.getMetadata().getStart());
	}

	@Override
	public IPredicate<? super T> getQueryPredicate() {
		return null;
	}

	@Override
	public IPredicate<? super T> getRemovePredicate() {
		return null;
	}

	@Override
	public void init() {
	}

	@Override
	public void insert(T element) {
		Number number = (Number) element.getAttribute(storedIdPosition);
		this.elements.put(number.longValue(), element);
	}

	@Override
	public void insertAll(List<T> toBeInserted) {
		for (T curElement : toBeInserted) {
			insert(curElement);
		}
	}

	@Override
	public boolean isEmpty() {
		return this.elements.isEmpty();
	}

	@Override
	public T peek() {
		throw new UnsupportedOperationException();
	}

	@Override
	public T poll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void purgeElements(T element, de.uniol.inf.is.odysseus.core.Order order) {
		if (element.getMetadata().getPriority() == 0) {
			purgeElementsBefore(element.getMetadata().getStart());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> query(T element, de.uniol.inf.is.odysseus.core.Order order) {
		Number pos = element.getAttribute(externalIdPosition);
		Collection<T> idFits = this.elements.get(pos.longValue());

		if (idFits == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		LinkedList<T> results = new LinkedList<T>(idFits);
		Iterator<T> it = results.iterator();
		while (it.hasNext()) {
			T nextElement = it.next();
			if (!TimeInterval.overlaps(element.getMetadata(), nextElement.getMetadata())) {
				it.remove();
			}
		}
		return results.iterator();
	}

	@Override
	public Iterator<T> queryCopy(T element, de.uniol.inf.is.odysseus.core.Order order, boolean extract) {
		return query(element, order);
	}

	@Override
	public boolean remove(T element) {
		return this.elements.removeAll(element.getAttribute(storedIdPosition)) != null;
	}

	@Override
	public void removeAll(List<T> toBeRemoved) {
		for (T curElement : toBeRemoved) {
			this.elements.removeAll(curElement);
		}
	}

	@Override
	public void setQueryPredicate(IPredicate<? super T> updatePredicate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setRemovePredicate(IPredicate<? super T> removePredicate) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		return this.elements.size();
	}

	@Override
	public Iterator<T> iterator() {
		return this.elements.values().iterator();
	}

	@Override
	public PriorityIdHashSweepArea<K, T> clone() {
		return new PriorityIdHashSweepArea<K, T>(this);
	}

	@Override
	public PointInTime getMaxStartTs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PointInTime getMinStartTs() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMaxEndTs()
	 */
	@Override
	public PointInTime getMaxEndTs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "PrioIdHashSA";
	}

	@Override
	public T peekLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> extractAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> extractAllElementsAsList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> extractElementsStartingBefore(PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> queryElementsStartingBefore(PointInTime validity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> extractElementsStartingBeforeOrEquals(PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> extractElementsStartingEquals(PointInTime validity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryOverlapsAsList(ITimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> queryOverlaps(ITimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryContains(PointInTime point) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<T> extractOverlaps(ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> extractOverlapsAsList(ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSweepAreaAsString(String tab, int max, boolean tail) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<T> queryOverlapsAsListExtractOutdated(ITimeInterval interval, List<T> outdated) {
		throw new UnsupportedOperationException();
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
