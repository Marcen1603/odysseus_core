package de.uniol.inf.is.odysseus.mining;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.AbstractStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class TupleList<M extends ITimeInterval> extends AbstractStreamObject<M> implements Comparable<TupleList<M>> {

	private static final long serialVersionUID = 1499234675223462600L;

	// private PriorityQueue<Tuple<M>> list = new PriorityQueue<>(1, new MetadataComparator<ITimeInterval>());
	private ArrayList<Tuple<M>> list = new ArrayList<>();

	public TupleList(TupleList<M> clone) {
		super();
		this.list = new ArrayList<>(clone.list);
	}

	public TupleList(Tuple<M> first) {
		super();
		this.list.add(first);
	}

	public void add(Tuple<M> t) {
		list.add(t);
	}

	public void remove(Tuple<M> t) {
		list.remove(t);
	}

	public void addAll(Collection<Tuple<M>> tuples) {
		list.addAll(list);
	}

	public Iterable<Tuple<M>> getList() {
		return list;
	}

	@Override
	public TupleList<M> clone() {
		return new TupleList<>(this);
	}

	@Override
	public String toString() {
		return this.list.size() + " Elements [" + getMetadata().getStart() + "|" + getMetadata().getEnd() + "): " + this.list.toString() + " META: " + getMetadata().toString();
	}

	public boolean equalElements(TupleList<?> other) {
		if (this.list.size() == other.list.size()) {
			return this.list.containsAll(other.list) && other.list.containsAll(this.list);
		}
		return false;
		// /return this.list.equals(other.list);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object arg) {
		if (arg != null) {
			if (arg instanceof TupleList) {
				TupleList<?> other = (TupleList<?>) arg;
				if (this.equalElements(other)) {
					// return this.getMetadata().equals(other.getMetadata());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int compareTo(TupleList<M> o) {
		if (this.equalElements(o)) {
			return 0;
		} else {
			if (this.list.size() < o.list.size()) {
				return -1;
			} else {
				return 1;
			}
		}
	}

}
