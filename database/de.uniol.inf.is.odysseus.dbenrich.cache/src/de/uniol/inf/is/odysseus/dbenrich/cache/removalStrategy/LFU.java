package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;

/**
 * This class implements a "least frequently used" caching strategy.
 */
public class LFU implements IRemovalStrategy {
	
	private Map<?, CacheEntry<?>> cacheStore;
	
	private PriorityQueue<ListEntry> priorityQueue;

	public LFU() {
		// Needed for the RemovalStrategyRegistry
	}

	private LFU(Map<?, CacheEntry<?>> cacheStore) {
		this.cacheStore = cacheStore;
		priorityQueue = new PriorityQueue<>(20, new ListEntryComparator());
	}

	@Override
	public String getName() {
		return "LFU";
	}

	@Override
	public void notifyNew(Object key, CacheEntry<?> value) {
		priorityQueue.offer(new ListEntry(key, value));
	}
	
	@Override
	public void notifyAccess(Object key, CacheEntry<?> value) {
		ListEntry accessedCopy = new ListEntry(key, value);
		priorityQueue.remove(accessedCopy);
		priorityQueue.offer(accessedCopy);
	}

	@Override
	public void removeNext() {
		// Remove element from the head of the queue and the cache.
		Object key = priorityQueue.remove().key;
		cacheStore.remove(key);
	}
	
	@Override
	public void clear() {
		priorityQueue.clear();
		cacheStore.clear();
	}

	@Override
	public IRemovalStrategy createInstance(Map<?, CacheEntry<?>> cacheStore) {
		return new LFU(cacheStore);
	}
	
	private class ListEntry {
		Object key;
		CacheEntry<?> cacheEntry;
		public ListEntry(Object key, CacheEntry<?> cacheEntry) {
			this.key = key;
			this.cacheEntry = cacheEntry;
		}
		@Override
		public boolean equals(Object obj) {
			// (null never happens)
			return key.equals(((ListEntry)obj).key);
		}
	}
	
	private class ListEntryComparator implements Comparator<ListEntry> {
		@Override
		public int compare(ListEntry a, ListEntry b) {
			return Long.compare(
					a.cacheEntry.getAccessCount(),
					b.cacheEntry.getAccessCount());
		}
	}
}
