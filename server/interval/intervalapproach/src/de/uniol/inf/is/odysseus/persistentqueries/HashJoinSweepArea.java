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

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.LinkedHashMultimap;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

/**
 * This sweep area is used for equi joins over non-windowed streams.
 * Instead of a list, it uses a multi attribute hash map
 * @author Andre Bolles, Cornelius Ludmann
 *
 */
//@SuppressWarnings({"unchecked","rawtypes"})
public class HashJoinSweepArea implements ITimeIntervalSweepArea<Tuple<? extends ITimeInterval>> {

	private static final long serialVersionUID = -8331551296317426445L;
	
	private LinkedHashMultimap<Object, Tuple<? extends ITimeInterval>> elements;
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
	
	@Override
	public ISweepArea<Tuple<? extends ITimeInterval>> newInstance(OptionMap options) {
		throw new UnsupportedOperationException();
	}
	
	public HashJoinSweepArea(int[] insertRestrictList, int[] queryRestrictList){
		this.elements = LinkedHashMultimap.create();		
		this.insertRestrictList = insertRestrictList;
		this.queryRestrictList = queryRestrictList;
	}
	
	public HashJoinSweepArea(HashJoinSweepArea original){
		this.elements = original.elements;
		this.insertRestrictList = original.insertRestrictList;
		this.queryRestrictList = original.queryRestrictList;
	}
	
	protected static Object getKey(Tuple<? extends ITimeInterval> element, int[] restrictList) {
		if(restrictList.length == 1) {
			// TODO: save copy?
			return element.getAttribute(restrictList[0]);
		} else {
			return element.restrict(restrictList, true);
		}
	}
	
	@Override
	public void insert(Tuple<? extends ITimeInterval> element) {
		Object key = getKey(element, insertRestrictList);
		synchronized (this.elements) {
			this.elements.put(key, element);
		}
		
	}
	
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryCopy(Tuple<? extends ITimeInterval> element, Order order, boolean extract) {
		if(this.elements.isEmpty()) {
			return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
		}
		
		LinkedList<Tuple<? extends ITimeInterval>> result;
		synchronized(this.elements){
			Object key = getKey(element, queryRestrictList);
			if(!this.elements.containsKey(key))
				return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
			
			result = new LinkedList<Tuple<? extends ITimeInterval>>();
			Collection<Tuple<? extends ITimeInterval>> matchingTuples = this.elements.get(key);
			
			for(Tuple<? extends ITimeInterval> mathingTuple: matchingTuples){
					if(extract) {
						result.add(mathingTuple);
					} else {
						result.add(mathingTuple.clone());
					}
			}
			if(extract)
				this.elements.removeAll(key);
		}

		return result.iterator();
	}
	
	/* non-windowed data needs no purging.
	 * 
	 */
	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElements(Tuple<? extends ITimeInterval> element, Order order){
		return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
	}
	
	@Override
	public void purgeElements(Tuple<? extends ITimeInterval> element, Order order){
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> iterator() {
		return this.elements.values().iterator();
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
			de.uniol.inf.is.odysseus.core.Order order) {
		if(this.elements.isEmpty()) {
			return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
		}
		
		synchronized(this.elements){
			Object key = getKey(element, queryRestrictList);
			if(!this.elements.containsKey(key))
				return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
			
			Collection<Tuple<? extends ITimeInterval>> matchingTuples = this.elements.get(key);
			return matchingTuples.iterator();
		}
	}

	@Override
	public Tuple<? extends ITimeInterval> peek() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<? extends ITimeInterval> poll() {
		Tuple<? extends ITimeInterval> retVal = null;
		Iterator<Tuple<? extends ITimeInterval>> iter = this.elements.values().iterator();
		if(iter.hasNext()){
			retVal = iter.next();
			iter.remove();
		}
		
		return retVal;
	}

	@Override
	public boolean remove(Tuple<? extends ITimeInterval> element) {
		Object key = getKey(element, insertRestrictList);
		synchronized(this.elements){
			return this.elements.remove(key, element);
		}
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
		return null;
	}

	@Override
	public void setQueryPredicate(IPredicate<? super Tuple<? extends ITimeInterval>> queryPredicate) {
	}

	@Override
	public void setRemovePredicate(IPredicate<? super Tuple<? extends ITimeInterval>> removePredicate) {
	}
	
	@Override
	public IPredicate<? super Tuple<? extends ITimeInterval>> getRemovePredicate() {
		return null;
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
		return Collections.<Tuple<? extends ITimeInterval>>emptyList().iterator();
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea#getMaxEndTs()
	 */
	@Override
	public PointInTime getMaxEndTs() {
		return PointInTime.INFINITY;
	}
	
	@Override
	public String getName() {
		return "HashJoinSA";
	}

	@Override
	public Tuple<? extends ITimeInterval> peekLast() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractAllElements() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingBefore(PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryElementsStartingBefore(PointInTime validity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingBeforeOrEquals(PointInTime time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractElementsStartingEquals(PointInTime validity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> queryOverlapsAsList(ITimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> queryOverlaps(ITimeInterval interval) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<Tuple<? extends ITimeInterval>> extractOverlaps(ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Tuple<? extends ITimeInterval>> extractOverlapsAsList(ITimeInterval t) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSweepAreaAsString(PointInTime baseTime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSweepAreaAsString(String tab, int max, boolean tail) {
		// TODO Auto-generated method stub
		return null;
	}
}
