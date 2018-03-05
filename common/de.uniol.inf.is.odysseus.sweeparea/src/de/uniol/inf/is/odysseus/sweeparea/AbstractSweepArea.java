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
package de.uniol.inf.is.odysseus.sweeparea;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractSweepArea<T extends IStreamObject<?>> implements ISweepArea<T>, Serializable {

	private static final long serialVersionUID = 1054820871977159904L;

	// elements get stored in a linked list instead of a priority queue
	// because we need ordered traversal via iterator
	// while insertion is in O(N), it is not that bad in reality, because
	// the inserts are typically chronologically ordered
	private final IFastList<T> elements;

	protected Comparator<? super T> comparator;

	private transient IPredicate<? super T> queryPredicate;

	private transient IPredicate<? super T> removePredicate;

	private String areaName;

	/**
	 * Creates query results on the fly by iterating over the elements and applying
	 * the query predicate. Make sure, you don't modify the sweeparea while
	 * iterating, otherwise you might get unexpected results.
	 * 
	 * @author Jonas Jacobi
	 */
	private class QueryIterator implements Iterator<T> {
		private Iterator<T> it;

		T currentElement;

		private Order order;

		private T element;

		public QueryIterator(T element, Order order) {
			this.it = getElements().iterator();
			this.order = order;
			this.element = element;
		}

		@Override
		public boolean hasNext() {
			try {
				if (this.currentElement != null) {
					return true;
				}
				switch (order) {
				case LeftRight:
					while (it.hasNext()) {
						T next = it.next();
						if (queryPredicate.evaluate(element, next)) {
							this.currentElement = next;
							return true;
						}

					}
					break;
				case RightLeft:
					while (it.hasNext()) {
						T next = it.next();
						if (queryPredicate.evaluate(next, element)) {
							this.currentElement = next;
							return true;
						}
					}
					break;
				}
			} catch (NullPointerException e) {
				// ignore null pointer could be when value in attribute is null
				// e.printStackTrace();
			}
			this.currentElement = null;
			return false;

		}

		@Override
		public T next() {
			if (this.currentElement != null) {
				T tmpElement = this.currentElement;
				this.currentElement = null;
				return tmpElement;
			}
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return next();
		}

		@Override
		public void remove() {
			this.it.remove();
		}

	}

	public AbstractSweepArea() {
		elements = new FastArrayList<>();
	}

	public AbstractSweepArea(IFastList<T> elements, Comparator<? super T> comparator) {
		this.elements = elements;
		this.comparator = comparator;
		// this.saSupervisor = new SweepAreaSupervisor(this, 20000);
		// this.saSupervisor.start();
	}

	@Override
	public void setAreaName(String name) {
		this.areaName = name;
	}

	@Override
	public String getAreaName() {
		return areaName;
	}

	@SuppressWarnings("unchecked")
	public AbstractSweepArea(AbstractSweepArea<T> area) throws InstantiationException, IllegalAccessException {
		this.elements = area.elements.getClass().newInstance();
		this.elements.addAll(area.elements);
		this.getElements().addAll(area.getElements());
		this.comparator = area.comparator;
		this.queryPredicate = area.queryPredicate.clone();
		this.removePredicate = area.removePredicate.clone();
	}

	@Override
	public void insert(T s) {
		if (this.comparator == null) {
			this.getElements().add(s);
			return;
		}
		ListIterator<T> li = this.getElements().listIterator(this.getElements().size());
		// starts from end and inserts the element s if li.previous is at least
		// equal (<=0) to s
		// 0 instead of -1 ensures that the area is insertion safe
		while (li.hasPrevious()) {
			if (this.comparator.compare(li.previous(), s) <= 0) {
				li.next();
				li.add(s);
				return;
			}
		}
		this.getElements().add(0, s);
	}

	@Override
	public Iterator<T> query(T element, Order order) {
		return new QueryIterator(element, order);
	}

	@Override
	public Iterator<T> queryCopy(T element, Order order, boolean extract) {
		LinkedList<T> result = new LinkedList<T>();
		switch (order) {
		case LeftRight: {
			Iterator<T> iter = this.getElements().iterator();
			while (iter.hasNext()) {
				T next = iter.next();
				if (queryPredicate.evaluate(element, next)) {
					result.add(next);
					if (extract) {
						iter.remove();
					}
				}
			}
		}
		break;
		
		case RightLeft: {
			Iterator<T> iter = this.getElements().iterator();
			while (iter.hasNext()) {
				T next = iter.next();
				if (queryPredicate.evaluate(next, element)) {
					result.add(next);
					if (extract) {
						iter.remove();
					}
				}
			}
		}
		break;
		
		}
		return result.iterator();
	}

	@Override
	public Iterator<T> extractElements(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		Iterator<T> it = this.getElements().iterator();
		switch (order) {
		case LeftRight:
			while (it.hasNext()) {
				T next = it.next();
				if (removePredicate.evaluate(element, next)) {
					it.remove();
					result.add(next);
				}
			}
			break;
		case RightLeft:
			while (it.hasNext()) {
				T next = it.next();
				if (removePredicate.evaluate(next, element)) {
					it.remove();
					result.add(next);
				}
			}
			break;
		}
		return result.iterator();
	}

	@Override
	public void purgeElements(T element, Order order) {
		Iterator<T> it = this.getElements().iterator();
		switch (order) {
		case LeftRight:
			while (it.hasNext()) {
				if (removePredicate.evaluate(element, it.next())) {
					it.remove();
				}
			}
			break;
		case RightLeft:
			while (it.hasNext()) {
				if (removePredicate.evaluate(it.next(), element)) {
					it.remove();
				}
			}
			break;
		}
	}

	@Override
	public IPredicate<? super T> getRemovePredicate() {
		return removePredicate;
	}

	@Override
	public void setRemovePredicate(IPredicate<? super T> removePredicate) {
		this.removePredicate = removePredicate;
	}

	@Override
	public IPredicate<? super T> getQueryPredicate() {
		return queryPredicate;
	}

	@Override
	public void setQueryPredicate(IPredicate<? super T> updatePredicate) {
		this.queryPredicate = updatePredicate;
	}

	@Override
	final public boolean isEmpty() {
		return this.getElements().isEmpty();
	}

	@Override
	final public int size() {
		return this.getElements().size();
	}

	@Override
	final public void clear() {
		this.getElements().clear();
	}

	@Override
	final public Iterator<T> iterator() {
		return this.getElements().iterator();
	}

	@Override
	public boolean remove(T element) {
		return this.getElements().remove(element);
	}

	@Override
	public T peek() {
		return this.getElements().get(getElements().size() - 1);
	}

	@Override
	public T poll() {
		return this.getElements().remove(getElements().size() - 1);
	}

	/**
	 * This method inserts a multiple elements into the sweep area.
	 * 
	 * @param toBeInserted
	 *            the elements to be inserted
	 */
	@Override
	public void insertAll(List<T> toBeInserted) {
		if (toBeInserted.isEmpty()) {
			return;
		}
		if (this.comparator == null) {
			this.getElements().addAll(toBeInserted);
			return;
		}
		Collections.sort(toBeInserted, this.comparator);

		ListIterator<T> listToInsertInto = this.getElements().listIterator();
		ListIterator<T> listToInsert = toBeInserted.listIterator();

		// Merge sort
		while (listToInsert.hasNext()) {
			T currentToInsert = listToInsert.next();
			boolean found = false;
			while (listToInsertInto.hasNext() && !found) {
				if (this.comparator.compare(currentToInsert, listToInsertInto.next()) == -1) {
					listToInsertInto.previous();
					found = true;
				}
			}
			listToInsertInto.add(currentToInsert);
		}
	}

	/**
	 * This method removes all specified elements from the sweep area.
	 * 
	 * @param toBeRemoved
	 *            the elements to be removed
	 */
	@Override
	public void removeAll(List<T> toBeRemoved) {
		this.getElements().removeAll(toBeRemoved);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("SweepArea " + getElements().size() + " Elems \n");
		for (T element : getElements()) {
			buf.append(element).append(" ");
			buf.append("{META ").append(element.getMetadata().toString()).append("}\n");
		}
		return buf.toString();
	}

	@Override
	public void init() {
	}

	@Override
	abstract public ISweepArea<T> clone();

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	final public boolean equals(Object obj) {
		return super.equals(obj);
	}

	protected IFastList<T> getElements() {
		return elements;
	}
}
