package de.uniol.inf.is.odysseus.cache.removalstrategy;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import de.uniol.inf.is.odysseus.cache.CacheEntry;

//note: based on a copy of de.uniol.inf.is.odysses.dbenrich.cache.LRU
public class LRU implements IRemovalStrategy {
	
	/**
	 * cache store for the management of the removal strategy
	 */
	private Map<Object, CacheEntry> cacheStore;
	
	/**
	 * Structure to get the correct cache Entry to remove
	 */
	private PriorityQueue<ListEntry> priorityQueue;
	
	/**
	 * Constructor for the LRU removal strategy.
	 * The fist element which was last accessed will be removed 
	 */
	public LRU() {
		//Needed for the RemovalStrategyRegistry
	}
	
	private LRU(Map<Object, CacheEntry> cacheStore) {
		this.cacheStore = cacheStore;
		this.priorityQueue = new PriorityQueue<>(20, new ListEntryComparator());
	}
	@Override
	public String getName() {
		return "LRU";
	}

	@Override
	public void notifyNew(Object key, CacheEntry value) {
		this.priorityQueue.offer(new ListEntry(key, value));
	}

	@Override
	public void notifyAccess(Object key, CacheEntry value) {
		ListEntry accessedCopy = new ListEntry(key, value);
		this.priorityQueue.remove(accessedCopy);
		this.priorityQueue.offer(accessedCopy);
	}

	@Override
	public CacheEntry removeNext() {
		Object key = this.priorityQueue.remove().key;
		CacheEntry removedEntry = this.cacheStore.get(key);
		this.cacheStore.remove(key);
		return removedEntry;
	}

	@Override
	public void clear() {
		this.priorityQueue.clear();
		this.cacheStore.clear();
	}

	@Override
	public IRemovalStrategy createInstance(Map<Object, CacheEntry> cacheStore) {
		return new LRU(cacheStore);
	}
	
	public class ListEntry {
		Object key;
		CacheEntry cacheEntry;
		
		public ListEntry(Object key, CacheEntry cacheEntry) {
			this.key = key;
			this.cacheEntry = cacheEntry;
		}
		@Override
		public boolean equals(Object obj) {
			return this.key.equals(((ListEntry)obj).key);
		}
		
		@Override
		public int hashCode() {
			return key.hashCode();
		}
	}
	
	public class ListEntryComparator implements Comparator<ListEntry> {
		@Override
		public int compare(ListEntry a, ListEntry b) {
			return Long.compare(
					a.cacheEntry.getLastAccesTime(),
					b.cacheEntry.getLastAccesTime());
		}
	}

}
