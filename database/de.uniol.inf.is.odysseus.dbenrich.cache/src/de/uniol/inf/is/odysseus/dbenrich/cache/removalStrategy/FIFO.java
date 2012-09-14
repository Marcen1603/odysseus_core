package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;

public class FIFO extends AbstractRemovalStrategy {

	private Queue<Object> queue;

	public FIFO() {
		// Needed for the RemovalStrategyRegistry
	}

	private FIFO(Map<?, CacheEntry<?>> cacheStore) {
		super.cacheStore = cacheStore;
		queue = new LinkedList<>();
	}

	@Override
	public void notifyNew(Object key, CacheEntry<?> value) {
		// Insert as last element in queue
		queue.offer(key);
	}
	
	@Override
	public void notifyAccess(Object key, CacheEntry<?> value) {
		// do nothing
	}

	@Override
	public void removeNext() {
		// Remove element from the head of the queue and the cache.
		Object key = queue.remove();
		cacheStore.remove(key);
	}
	
	@Override
	public void clear() {
		queue.clear();
		cacheStore.clear();
	}

	@Override
	public String getName() {
		return "FIFO";
	}

	@Override
	public IRemovalStrategy createInstance(Map<?, CacheEntry<?>> cacheStore) {
		return new FIFO(cacheStore);
	}
}
