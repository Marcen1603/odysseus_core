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
package de.uniol.inf.is.odysseus.mining.frequentitem.apriori;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.Transaction;

/**
 * @author Dennis Geesen
 * 
 */
public class FrequentItemSetContainer<T, M extends ITimeInterval> {

	private HashMap<FrequentItemSet<T, M>, Integer> items = new HashMap<FrequentItemSet<T, M>, Integer>();
	private M metadata;

	public FrequentItemSetContainer() {

	}

	public FrequentItemSetContainer(FrequentItemSetContainer<T,M> old) {
		for (Entry<FrequentItemSet<T, M>, Integer> e : old.items.entrySet()) {
			this.items.put(e.getKey().clone(), e.getValue());
		}
	}

	public void addItemSet(FrequentItemSet<T, M> item) {
		synchronized (this.items) {
			this.items.put(item, 1);
		}
	}

	public Set<FrequentItemSet<T, M>> getFrequentItemSets() {
		return Collections.unmodifiableSet(this.items.keySet());
	}

	public boolean containsFrequentItemSet(FrequentItemSet<T, M> fis) {
		return this.items.containsKey(fis);
	}
	
	
	public boolean containsAll(List<FrequentItemSet<T, M>> subsets){
		return this.items.keySet().containsAll(subsets);
	}

	public void increaseCount(FrequentItemSet<T, M> item) {
		synchronized (this.items) {
			int newCount = this.items.get(item) + 1;
			this.items.put(item, newCount);
		}
	}

	public FrequentItemSet<T, M> extractFrequentItemSet() {
		Iterator<FrequentItemSet<T, M>> iter = this.items.keySet().iterator();
		FrequentItemSet<T, M> item = iter.next();
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
	public FrequentItemSetContainer<T,M> clone() {
		return new FrequentItemSetContainer<T,M>(this);
	}

	/**
	 * @param minsupport
	 */
	public void purgeFrequentItemWithoutMinimumSupport(int minsupport) {
		synchronized (this.items) {
			HashMap<FrequentItemSet<T, M>, Integer> newitems = new HashMap<FrequentItemSet<T, M>, Integer>();
			for (Entry<FrequentItemSet<T, M>, Integer> entry : items.entrySet()) {
				if (entry.getValue() >= minsupport) {
					newitems.put(entry.getKey(), entry.getValue());
				}
			}
			this.items = newitems;
		}
	}

	public void calcSupportCount(List<Transaction<M>> transactions) {
		synchronized (items) {
			for (FrequentItemSet<T, M> itemset : this.items.keySet()) {
				int count = 0;
				for (Transaction<M> transaction : transactions) {
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
		for (Entry<FrequentItemSet<T, M>, Integer> entry : items.entrySet()) {
			s = s + "(" + entry.getValue() + ") " + entry.getKey() + "\n";
		}
		return s;
	}

	/**
	 * @return the metadata
	 */
	public M getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(M metadata) {
		this.metadata = metadata;
	}

}
