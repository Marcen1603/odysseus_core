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
package de.uniol.inf.is.odysseus.mining.frequentitem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author Dennis Geesen
 * 
 */
public class FrequentItemSetContainer<T> {

	private HashMap<FrequentItemSet<T>, Integer> items = new HashMap<FrequentItemSet<T>, Integer>();

	public FrequentItemSetContainer() {

	}

	public FrequentItemSetContainer(FrequentItemSetContainer<T> old) {
		for (Entry<FrequentItemSet<T>, Integer> e : old.items.entrySet()) {
			this.items.put(e.getKey().clone(), e.getValue());
		}
	}

	public void addItemSet(FrequentItemSet<T> item) {
		synchronized (this.items) {
			this.items.put(item, 1);
		}
	}

	public Set<FrequentItemSet<T>> getFrequentItemSets() {
		return Collections.unmodifiableSet(this.items.keySet());
	}

	public boolean containsFrequentItemSet(FrequentItemSet<T> fis) {
//		System.out.println("-----------------------------------------");
//		System.out.println("check "+this+" and "+fis);
		boolean result = this.items.containsKey(fis);
//		System.out.println("result: "+result);
//		System.out.println("-----------------------------------------");
		return result;
//		for (FrequentItemSet<T> item : this.items.keySet()) {
//			if (item.equals(fis)) {
//				return true;
//			}
//		}
//		return false;
	}
	
	
	public boolean containsAll(List<FrequentItemSet<T>> subsets){
		return this.items.keySet().containsAll(subsets);
//		for(FrequentItemSet<T> fis : subsets){
//			if(!containsFrequentItemSet(fis)){
//				return false;
//			}
//		}		
//		return true;
	}

	public void increaseCount(FrequentItemSet<T> item) {
		synchronized (this.items) {
			int newCount = this.items.get(item) + 1;
			this.items.put(item, newCount);
		}
	}

	public FrequentItemSet<T> extractFrequentItemSet() {
		Iterator<FrequentItemSet<T>> iter = this.items.keySet().iterator();
		FrequentItemSet<T> item = iter.next();
		iter.remove();
		return item;
	}

	public int size() {
		return this.items.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public FrequentItemSetContainer<T> clone() {
		return new FrequentItemSetContainer<T>(this);
	}

	/**
	 * @param minsupport
	 */
	public void purgeFrequentItemWithoutMinimumSupport(int minsupport) {
		synchronized (this.items) {
			HashMap<FrequentItemSet<T>, Integer> newitems = new HashMap<FrequentItemSet<T>, Integer>();
			for (Entry<FrequentItemSet<T>, Integer> entry : items.entrySet()) {
				if (entry.getValue() >= minsupport) {
					newitems.put(entry.getKey(), entry.getValue());
				}
			}
			this.items = newitems;
		}
	}

	public void calcSupportCount(List<Transaction<T>> transactions) {
		synchronized (items) {
			for (FrequentItemSet<T> itemset : this.items.keySet()) {
				int count = 0;
				for (Transaction<T> transaction : transactions) {
					if (itemset.isSubsetOf(transaction)) {
						count++;
					}
				}
				this.items.put(itemset, count);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		for (Entry<FrequentItemSet<T>, Integer> entry : items.entrySet()) {
			s = s + "(" + entry.getValue() + ")" + entry.getKey() + "\n";
		}
		return s;
	}

}
