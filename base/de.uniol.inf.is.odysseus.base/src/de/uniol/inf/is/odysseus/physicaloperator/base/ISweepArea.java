package de.uniol.inf.is.odysseus.physicaloperator.base;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.predicate.IPredicate;

public interface ISweepArea<T> extends Iterable<T> {

	/**
	 * LeftRight is the same as isLeftArea == false RightLeft is the same as
	 * isLeftArea == true
	 * 
	 * In a join, the sweepArea method queryCopy will be called with LeftRight,
	 * if the passed element is from the left input stream. It will be called
	 * with RightLeft, if the passed element is from the right input stream.
	 * 
	 */
	public static enum Order {
		LeftRight, RightLeft;
		public Order inverse() {
			if (this.ordinal() == LeftRight.ordinal()) {
				return RightLeft;
			} else {
				return LeftRight;
			}
		}

		public static Order fromOrdinal(int i) {
			switch (i) {
			case 0:
				return LeftRight;
			case 1:
				return RightLeft;
			default:
				throw new IllegalArgumentException(
						"illegal ordinal value for Order");
			}
		}
	}

	public void init();

	public void insert(T element);

	public void insertAll(List<T> toBeInserted);

	public void purgeElements(T element, Order order);

	public void clear();

	/**
	 * 
	 * @param element
	 *            The new element from an input stream
	 * @param order
	 *            LeftRight, if element is from the left input stream RightLeft,
	 *            if element is from the right input stream
	 * @return
	 */
	public Iterator<T> query(T element, Order order);

	/**
	 * same as above, but you are guaranteed to iterate over your own copy of
	 * the query results
	 */
	public Iterator<T> queryCopy(T element, Order order);

	public Iterator<T> extractElements(T element, Order order);

	public T peek();

	public T poll();

	public boolean remove(T element);

	public void removeAll(List<T> toBeRemoved);

	public boolean isEmpty();

	public int size();

	public IPredicate<? super T> getQueryPredicate();

	public void setQueryPredicate(IPredicate<? super T> updatePredicate);
	
	public ISweepArea<T> clone()  throws CloneNotSupportedException;
}
