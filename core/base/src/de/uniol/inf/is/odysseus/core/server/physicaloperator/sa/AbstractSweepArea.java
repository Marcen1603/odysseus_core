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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.sa;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractSweepArea<T extends IMetaAttributeContainer<?>> implements ITemporalSweepArea<T> {
	// elements get stored in a linked list instead of a priority queue
	// because we need ordered traversal via iterator
	// while insertion is in O(N), it is not that bad in reality, because
	// the inserts are typically chronologically ordered
	private final LinkedList<T> elements = new LinkedList<T>();

	private Comparator<? super T> comparator;

	IPredicate<? super T> queryPredicate;

	private IPredicate<? super T> removePredicate;
	
	/**
	 * Creates query results on the fly by iterating over the elements and
	 * applying the query predicate. Make sure, you don't modify the sweeparea
	 * while iterating, otherwise you might get unexpected results.
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
	}

	public AbstractSweepArea(Comparator<? super T> comparator) {
		this.comparator = comparator;
//		this.saSupervisor = new SweepAreaSupervisor(this, 20000);
//		this.saSupervisor.start();
	}

	public AbstractSweepArea(AbstractSweepArea<T> area) {
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
		// starts from end and inserts the element s if li.previous is at least equal (<=0) to s
		// 0 instead of -1 ensures that the area is insertion safe
		while (li.hasPrevious()) {
			if (this.comparator.compare(li.previous(), s) <= 0) {
				li.next();
				li.add(s);
				return;
			}
		}
		this.getElements().addFirst(s);
	}

	@Override
	public Iterator<T> query(T element, Order order) {
		return new QueryIterator(element, order);
	}

	@Override
	public Iterator<T> queryCopy(T element, Order order) {
		LinkedList<T> result = new LinkedList<T>();
		switch (order) {
		case LeftRight:
			for (T next : this.getElements()) {
				if (queryPredicate.evaluate(element, next)) {
					result.add(next);
				}
			}
			break;
		case RightLeft:
			for (T next : this.getElements()) {
				if (queryPredicate.evaluate(next, element)) {
					result.add(next);
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
		return this.getElements().peek();
	}
	
	@Override
	public T poll() {
		return this.getElements().poll();
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
		T first = toBeInserted.get(0);
		ListIterator<T> li = this.getElements().listIterator(this.getElements().size());
		while (li.hasPrevious()) {
			if (this.comparator.compare(li.previous(), first) == -1) {
				this.getElements().addAll(li.nextIndex(), toBeInserted);
				return;
			}
		}
		this.getElements().addAll(0, toBeInserted);
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
		StringBuffer buf = new StringBuffer("SweepArea " + getElements().size()
				+ " Elems \n");
		for (T element : getElements()) {
			buf.append(element).append(" ");
			buf.append("{META ").append(element.getMetadata().toString()).append("}\n");
		}
		return buf.toString();
	}
	
	@Override
	public void init(){
		this.queryPredicate.init();
		this.removePredicate.init();
	}
	
	@Override
	abstract public AbstractSweepArea<T> clone();

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	final public boolean equals(Object obj) {
		return super.equals(obj);
	}

	protected LinkedList<T> getElements() {
		return elements;
	}
	
	
}
