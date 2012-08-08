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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.Transaction;

/**
 * @author Dennis Geesen
 * 
 */
public class FrequentItemSet<T, M extends ITimeInterval>{

	private ArrayList<T> items = new ArrayList<T>();
	private M metadata;

	public FrequentItemSet(T item) {
		this.items.add(item);
	}

	/**
	 * @param frequentItemSet
	 */
	@SuppressWarnings("unchecked")
	public FrequentItemSet(FrequentItemSet<T,M> frequentItemSet) {
		this.items = new ArrayList<T>(frequentItemSet.items);
		this.metadata = (M) frequentItemSet.metadata.clone();
	}

	public void addFrequentItemSet(FrequentItemSet<T, M> set) {
		for (T item : set.items) {
			if (!this.items.contains(item)) {
				this.items.add(item);
			}
		}
	}

	public void addFrequentItem(T item) {
		this.items.add(item);
	}

	public void removeFrequentItem(T item) {
		this.items.remove(item);
	}

	public T getFrequentItem(T item) {
		for (T fi : this.items) {
			if (fi.equals(item)) {
				return fi;
			}
		}
		return null;
	}

	public boolean containsFrequentItem(T item) {
		return this.items.contains(item);
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof FrequentItemSet) {
			FrequentItemSet<?,?> other = (FrequentItemSet<?,?>) obj;
			return this.items.containsAll(other.items);			
		} else {
			return false;
		}
	}

	public int intersectCount(FrequentItemSet<T,M> other) {
		int count = 0;
		for (T item : this.items) {
			if (other.containsFrequentItem(item)) {
				count++;
			}
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "FrequentItemSet (";
		String sep = "";
		for (T fi : this.items) {
			s = s + sep+ fi;
			sep = " AND ";
		}
		s = s + ")";
		return s;
	}

	public boolean isSubsetOf(Transaction<M> transaction) {
		if (transaction.getElements().containsAll(items)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected FrequentItemSet<T,M> clone() {
		return new FrequentItemSet<T,M>(this);
	}

	/**
	 * splits this set of length k into subsets of length k-1 
	 */
	public List<FrequentItemSet<T,M>> splitIntoSmallerSubset() {		
		List<FrequentItemSet<T,M>> subsets = new ArrayList<FrequentItemSet<T,M>>();
		for(T item : this.items){
			FrequentItemSet<T,M> cl = this.clone();
			cl.removeFrequentItem(item);
			subsets.add(cl);
		}
		return subsets;
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
