package de.uniol.inf.is.odysseus.priority_interval;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.ITemporalSweepArea;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.priority.IPriority;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.util.LinkedMultiHashMap;

public class PriorityIdHashSweepArea<K extends ITimeInterval & IPriority, T extends RelationalTuple<K>>
		implements ITemporalSweepArea<T> {

	private LinkedMultiHashMap<Long, T> elements = new LinkedMultiHashMap<Long, T>();
	final private int storedIdPosition;
	final private int externalIdPosition;

	public PriorityIdHashSweepArea(int externalIdPosition, int storedIdPosition) {
		this.storedIdPosition = storedIdPosition;
		this.externalIdPosition = externalIdPosition;
	}

	public PriorityIdHashSweepArea(
			PriorityIdHashSweepArea<K, T> priorityIdHashSweepArea) {
		this.storedIdPosition = priorityIdHashSweepArea.storedIdPosition;
		this.externalIdPosition = priorityIdHashSweepArea.externalIdPosition;
		this.elements = priorityIdHashSweepArea.elements.clone();
	}

	@Override
	public Iterator<T> extractElementsBefore(PointInTime time) {
		Iterator<T> it = elements.valueIterator();
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
		return extractedElements.iterator();
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		Iterator<T> it = elements.valueIterator();
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
	public Iterator<T> extractElements(
			T element,
			de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order order) {
		return extractElementsBefore(element.getMetadata().getStart());
	}

	@Override
	public IPredicate<? super T> getQueryPredicate() {
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
	public void purgeElements(
			T element,
			de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order order) {
		if (element.getMetadata().getPriority() == 0) {
			purgeElementsBefore(element.getMetadata().getStart());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> query(
			T element,
			de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order order) {
		Number pos = element.getAttribute(externalIdPosition);
		Collection<T> idFits = this.elements.get(pos.longValue());

		if (idFits == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		LinkedList<T> results = new LinkedList<T>(idFits);
		Iterator<T> it = results.iterator();
		while (it.hasNext()) {
			T nextElement = it.next();
			if (!TimeInterval.overlaps(element.getMetadata(), nextElement
					.getMetadata())) {
				it.remove();
			}
		}
		return results.iterator();
	}

	@Override
	public Iterator<T> queryCopy(
			T element,
			de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order order) {
		return query(element, order);
	}

	@Override
	public boolean remove(T element) {
		return this.elements.remove(element.getAttribute(storedIdPosition)) != null;
	}

	@Override
	public void removeAll(List<T> toBeRemoved) {
		for (T curElement : toBeRemoved) {
			this.elements.remove(curElement);
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
		return this.elements.valueIterator();
	}

	@Override
	public PriorityIdHashSweepArea<K, T> clone() {
		return new PriorityIdHashSweepArea<K, T>(this);
	}

}
