package de.uniol.inf.is.odysseus.physicaloperator;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.IPredicate;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractSweepArea<T extends IMetaAttributeContainer<?>> implements ITemporalSweepArea<T> {
	// elements get stored in a linked list instead of a priority queue
	// because we need ordered traversal via iterator
	// while insertion is in O(N), it is not that bad in reality, because
	// the inserts are typically chronologically ordered
	protected LinkedList<T> elements;

	protected Comparator<? super T> comparator;

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
			this.it = elements.iterator();
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
			} else {
				if (hasNext()) {
					return next();
				} else {
					throw new NoSuchElementException();
				}
			}
		}

		@Override
		public void remove() {
			this.it.remove();
		}

	}

	public AbstractSweepArea() {
		this.elements = new LinkedList<T>();
//		this.saSupervisor = new SweepAreaSupervisor(this, 20000);
//		this.saSupervisor.start();
	}

	public AbstractSweepArea(Comparator<? super T> comparator) {
		this.comparator = comparator;
		this.elements = new LinkedList<T>();
//		this.saSupervisor = new SweepAreaSupervisor(this, 20000);
//		this.saSupervisor.start();
	}

	public AbstractSweepArea(AbstractSweepArea<T> area) {
		this.elements = new LinkedList<T>(area.elements);
		this.comparator = area.comparator;
		this.queryPredicate = area.queryPredicate.clone();
		this.removePredicate = area.removePredicate.clone();
	}
	
	@Override
	public void insert(T s) {
		if (this.comparator == null) {
			this.elements.add(s);
			return;
		}
		ListIterator<T> li = this.elements.listIterator(this.elements.size());
		while (li.hasPrevious()) {
			if (this.comparator.compare(li.previous(), s) == -1) {
				li.next();
				li.add(s);
				return;
			}
		}
		this.elements.addFirst(s);
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
			for (T next : this.elements) {
				if (queryPredicate.evaluate(element, next)) {
					result.add(next);
				}
			}
			break;
		case RightLeft:
			for (T next : this.elements) {
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
		Iterator<T> it = this.elements.iterator();
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
		Iterator<T> it = this.elements.iterator();
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
		return this.elements.isEmpty();
	}

	@Override
	final public int size() {
		return this.elements.size();
	}

	@Override
	final public void clear() {
		this.elements.clear();
	}

	@Override
	final public Iterator<T> iterator() {
		return this.elements.iterator();
	}

	@Override
	public boolean remove(T element) {
		return this.elements.remove(element);
	}

	@Override
	public T peek() {
		return this.elements.peek();
	}
	
	@Override
	public T poll() {
		return this.elements.poll();
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
			this.elements.addAll(toBeInserted);
			return;
		}
		Collections.sort(toBeInserted, this.comparator);
		T first = toBeInserted.get(0);
		ListIterator<T> li = this.elements.listIterator(this.elements.size());
		while (li.hasPrevious()) {
			if (this.comparator.compare(li.previous(), first) == -1) {
				this.elements.addAll(li.nextIndex(), toBeInserted);
				return;
			}
		}
		this.elements.addAll(0, toBeInserted);
	}

	/**
	 * This method removes all specified elements from the sweep area.
	 * 
	 * @param toBeRemoved
	 *            the elements to be removed
	 */
	@Override
	public void removeAll(List<T> toBeRemoved) {
		this.elements.removeAll(toBeRemoved);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("SweepArea " + elements.size()
				+ " Elems \n");
		for (T element : elements) {
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
	@SuppressWarnings("unchecked")
	public AbstractSweepArea<T> clone() {
		AbstractSweepArea<T> sa;
		try {
			sa = (AbstractSweepArea<T>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
		
		sa.elements = new LinkedList<T>(this.elements);
		sa.comparator = this.comparator;
		sa.queryPredicate = this.queryPredicate.clone();
		sa.removePredicate = this.removePredicate.clone();
		
		return sa;
	}
	
}
