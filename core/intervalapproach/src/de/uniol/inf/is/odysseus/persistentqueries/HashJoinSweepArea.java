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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.LinkedHashMultimap;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * This sweep area is used for equi joins over non-windowed streams.
 * Instead of a list, it uses a multi attribute hash map
 * @author Andre Bolles
 *
 */
@SuppressWarnings({"unchecked","rawtypes"})
public class HashJoinSweepArea implements ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> {

	private LinkedHashMultimap<Tuple<? extends ITimeInterval>, Pair<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>>> elements;
	/**
	 * This list is used for
	 * projecting new elements that
	 * are to be inserted into this
	 * sweep area to the attributes
	 * of equi join predicate
	 */
	int[] insertRestrictList;
	
	/**
	 * This list is use for projecting
	 * elements from the other side
	 * to the attributes of the equi join
	 * predicate. These attributes can
	 * have other indices in their schema
	 * than the attributes used to
	 * insert elements into this sweepArea.
	 * However, the values of the attributes
	 * can be equal and will then lead to
	 * a join of elements.
	 */
	int[] queryRestrictList;
	
	private static final PointInTime minMaxTs = new PointInTime(0);
	
	/**
	 * not used, only for compatibility and debug
	 */
	IPredicate<? super Tuple<? extends ITimeInterval>> queryPredicate;
	
	/**
	 * not used, only for compatibility and debug
	 */
	IPredicate<? super Tuple<? extends ITimeInterval>> removePredicate;
	
	public HashJoinSweepArea(int[] insertRestrictList, int[] queryRestrictList){
		this.elements = LinkedHashMultimap.create();		
		this.insertRestrictList = insertRestrictList;
		this.queryRestrictList = queryRestrictList;
	}
	
	public HashJoinSweepArea(HashJoinSweepArea original){
		this.elements = original.elements;
		this.insertRestrictList = original.insertRestrictList;
		this.queryRestrictList = original.queryRestrictList;
		this.removePredicate = original.removePredicate;
		this.queryPredicate = original.queryPredicate;
	}
	
	@Override
	public void insert(Tuple<? extends ITimeInterval> s) {
		synchronized (this.elements) {
			Tuple keyTuple = s.restrict(this.insertRestrictList, true);
			this.elements.put(keyTuple, new Pair(keyTuple, s));
		}
	}
	
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryCopy(Tuple<? extends ITimeInterval> element, Order order) {
		LinkedList<Tuple<? extends ITimeInterval>> result = new LinkedList<Tuple<? extends ITimeInterval>>();
		synchronized(this.elements){
			Tuple<? extends ITimeInterval> keyTuple = element.restrict(this.queryRestrictList, true);
			Collection<Pair<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>>> sameHashCode = this.elements.get(keyTuple);
			
			if(sameHashCode != null){ // otherwise there is no join partner
				for(Pair<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> pair: sameHashCode){
					if(keyTuple.equals(pair.getE1())){
						result.add(pair.getE2().clone());
					}
				}
			}
		}

		return result.iterator();
	}
	
	/* non-windowed data needs no purging.
	 * 
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElements(Tuple<? extends ITimeInterval> element, Order order){
		ArrayList<Tuple<? extends ITimeInterval>> emptyList = new ArrayList<Tuple<? extends ITimeInterval>>();
		return emptyList.iterator();
	}
	
	@Override
	public void purgeElements(Tuple<? extends ITimeInterval> element, Order order){
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> iterator() {
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insertAll(List<Tuple<? extends ITimeInterval>> toBeInserted) {
		for(Tuple<? extends ITimeInterval> r: toBeInserted){
			this.insert(r);
		}
		
	}

	@Override
	public void clear() {
		this.elements.clear();	
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> query(Tuple<? extends ITimeInterval> element,
			de.uniol.inf.is.odysseus.core.server.physicaloperator.sa.ISweepArea.Order order) {
		LinkedList<Tuple<? extends ITimeInterval>> result = new LinkedList<Tuple<? extends ITimeInterval>>();
		synchronized(this.elements){
			Tuple<? extends ITimeInterval> keyTuple = element.restrict(this.queryRestrictList, true);
			Collection<Pair<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>>> sameHashCode = this.elements.get(keyTuple);
			
			for(Pair<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>> pair: sameHashCode){
				if(keyTuple.equals(pair.getE1())){
					result.add(pair.getE2());
				}
			}
		}
		return result.iterator();
	}

	@Override
	public Tuple<? extends ITimeInterval> peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<? extends ITimeInterval> poll() {
		Tuple<? extends ITimeInterval> retVal = null;
		Iterator iter = this.elements.values().iterator();
		if(iter.hasNext()){
			retVal = ((Pair<Tuple<? extends ITimeInterval>, Tuple<? extends ITimeInterval>>) iter.next()).getE2();
			iter.remove();
		}
		
		return retVal;
		
	}

	@Override
	public boolean remove(Tuple<? extends ITimeInterval> element) {
		synchronized(this.elements){
			Tuple<? extends ITimeInterval> keyTuple = element.restrict(this.insertRestrictList, true);
			this.elements.remove(keyTuple, new Pair(keyTuple, element));
		}
		
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeAll(List<Tuple<? extends ITimeInterval>> toBeRemoved) {
		for(Tuple<? extends ITimeInterval> t: toBeRemoved){
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
		// TODO Auto-generated method stub
		return this.queryPredicate;
	}

	@Override
	public void setQueryPredicate(IPredicate<? super Tuple<? extends ITimeInterval>> queryPredicate) {
		this.queryPredicate = queryPredicate;
		
	}

	@Override
	public void setRemovePredicate(IPredicate<? super Tuple<? extends ITimeInterval>> removePredicate) {
		this.removePredicate = removePredicate;
	}
	
	@Override
    public HashJoinSweepArea clone(){
		return new HashJoinSweepArea(this);
	}

	@Override
	public void purgeElementsBefore(PointInTime time) {
		// do nothing
		// we need no purging here
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsBefore(PointInTime time) {
		// return an empty iterator, since there is no temporal ordering
		return new ArrayList().iterator();
	}

	@Override
	public PointInTime getMaxTs() {
		// always 0, since we assume same
		// time when using this sweep area
		return minMaxTs;
	}

	@Override
	public PointInTime getMinTs() {
		// always 0, since we assume same
		// time when using this sweep area
		return minMaxTs;
	}
	
	@Override
    public String toString(){
		String s = "";
		for(int i : this.insertRestrictList){
			s += i + " ";
		}
		
		s += " ||| ";
		
		for(int i : this.queryRestrictList){
			s += i + " ";
		}
		
		return s;
	}
}
