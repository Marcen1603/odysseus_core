/********************************************************************************** 
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
package de.uniol.inf.is.odysseus.sweeparea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;

/**
 * @author msalous
 *
 */

/**
 *This class represents an efficient online storage for different data streams.
 *The incoming tuples will be stored as partitions based on the comparator. 
 * 
 * */

public class PartitionedSweepArea<T extends IStreamObject<?>> implements
		ISweepArea<T>, Serializable {

	private static final long serialVersionUID = 348544350063903648L;

	private final IFastList<IFastList<T>> elements;
	private int listSize;
	protected Comparator<? super T> comparator;
	
	private transient IPredicate<? super T> queryPredicate;
	private transient IPredicate<? super T> removePredicate;
	
	private class QueryIterator implements Iterator<T> {
		
		private Iterator<T> it;
		private T currentElement;
		private Order order;
		private T element;

		public QueryIterator(T element, Order order) {
			for(IFastList<T> observations : getElements()){
				if(queryPredicate.evaluate(element, observations.get(0))){
					this.it = observations.iterator();
					break;
				}
			}
			this.order = order;
			this.element = element;
		}

		@Override
		public boolean hasNext() {
			if (this.currentElement != null) {
				return true;
			}
			if(this.it == null){
				return false;
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

	public PartitionedSweepArea() {
		this.elements = new FastArrayList<>();
		this.listSize = 10;
	}

	public PartitionedSweepArea(IFastList<IFastList<T>> elements, Comparator<? super T> comparator, int listSize) {
		this.elements = elements;
		this.comparator = comparator;
		this.listSize = listSize;
	}
	
	public PartitionedSweepArea(Comparator<? super T> comparator, int listSize) {
		this.elements = new FastArrayList<IFastList<T>>();
		this.comparator = comparator;
		this.listSize = listSize;
	}

	@SuppressWarnings("unchecked")
	public PartitionedSweepArea(PartitionedSweepArea<T> area) throws InstantiationException, IllegalAccessException {
		this.elements = area.elements.getClass().newInstance();
		this.getElements().addAll(area.getElements());
		this.comparator = area.comparator;
	}

	@Override
	public void insert(T element) {
		//element is a new observation of an existed object
		for(IFastList<T> list : getElements()){
			if(this.comparator.compare(element, list.get(0)) <= 0){
				list.add(element);
				if(list.size() > this.listSize)
					list.remove(0);
				return;
			}
		}
		//new object sensed
		IFastList<T> newList = new FastArrayList<>();
		newList.add(element);
		getElements().add(newList);
	}
	
	//not applicable for PartitionedSweepArea, because PartitionedSweepArea stores list of sensed objects observations, 
	//i.e. there is a last element for each list of observations   
	@Override
	public T peek() {
		return null;
	}

	//not applicable for PartitionedSweepArea, because PartitionedSweepArea stores list of sensed objects observations, 
		//i.e. there is a last element for each list of observations
	@Override
	public T poll() {
		return null;
	}

	@Override
	public Iterator<T> query(T element, Order order) {
		return new QueryIterator(element, order);
	}

	@Override
	public Iterator<T> queryCopy(T element, Order order, boolean extract) {
		LinkedList<T> result = new LinkedList<T>();
		switch (order) {
		case LeftRight:
			for (T next : this.getRelatedElements(element)) {
				if (queryPredicate.evaluate(element, next)) {
					result.add(next);
				}
			}
			break;
		case RightLeft:
			for (T next : this.getRelatedElements(element)) {
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
		Iterator<T> it = this.getRelatedElements(element).iterator();
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
		Iterator<T> it = this.getRelatedElements(element).iterator();
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
		if(getElements().size() == 0)
			return 0;
		int max = getElements().get(0).size();
		for(IFastList<T> list : getElements())
			if(list.size() > max)
				max = list.size();
		return max;
	}

	@Override
	final public void clear() {
		this.getElements().clear();
	}

	@Override
	final public Iterator<T> iterator() {
		return this.getElements().get(0).iterator();
	}

	@Override
	public boolean remove(T element) {
		return this.getRelatedElements(element).remove(element);
	}

	@Override
	public void insertAll(List<T> toBeInserted) {
	}

	@Override
	public void removeAll(List<T> toBeRemoved) {
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("Partitioned SweepArea with " + getElements().size()
				+ " elements for each object sensed \n");
		for(IFastList<T> elements : getElements()){
			for (T element : elements) {
				buf.append(element).append(" ");
				buf.append("{META ").append(element.getMetadata().toString())
						.append("}\n");
			}
			buf.append("\n");
		}
		return buf.toString();
	}

	@Override
	public void init() {
	}

	@Override
	public PartitionedSweepArea<T> clone(){
		try {
			return new PartitionedSweepArea<T>(this);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Clone error");
		}
	}
	
	@Override
	public ISweepArea<T> newInstance(OptionMap options) {
		return new PartitionedSweepArea<>();
	};

	@Override
	final public int hashCode() {
		return super.hashCode();
	}

	@Override
	final public boolean equals(Object obj) {
		return super.equals(obj);
	}

	protected IFastList<IFastList<T>> getElements() {
		return elements;
	}
	
	public IFastList<T> getRelatedElements(T element){
		for(IFastList<T> elements : getElements()){
			if (this.comparator.compare(elements.get(0), element) <=0 )
				return elements;
		}
		return null;
	}

	@Override
	public String getName() {
		return "PartitionedSweepArea";
	}
	
	public Iterator<T> queryOverlaps(ITimeInterval interval) {
		ArrayList<T> retList = new ArrayList<T>();
		synchronized (getElements()) {
			for(IFastList<T> list : getElements()){
				if (TimeInterval.overlaps((ITimeInterval)list.get(list.size()-1).getMetadata(), interval)) {
					retList.add(list.get(list.size()-1));
				}
			}
		}
		return retList.iterator();
	}
	
	public Iterator<T> getLastValuesIterator() {
		ArrayList<T> retList = new ArrayList<T>();
		synchronized (getElements()) {
			for(IFastList<T> list : getElements()){
					retList.add(list.get(list.size()-1));
			}
		}
		return retList.iterator();
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
	public String getAreaName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setAreaName(String name) {
		// TODO Auto-generated method stub
		
	}
}
