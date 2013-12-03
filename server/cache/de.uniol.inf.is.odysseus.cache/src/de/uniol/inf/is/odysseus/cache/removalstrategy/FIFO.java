package de.uniol.inf.is.odysseus.cache.removalstrategy;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import de.uniol.inf.is.odysseus.cache.CacheEntry;

//note: based on a copy of de.uniol.inf.is.odysses.dbenrich.cache.FIFO
public class FIFO implements IRemovalStrategy {
	
	/**
	 * cache store for the management of the removal strategy
	 */
	private Map<Object, CacheEntry> cacheStore;
	
	/**
	 * Structure to get the correct cache Entry to remove
	 */
	private Queue<Object> queue;
	
	/**
	 * Constructor for the FIFO removal strategy.
	 * The fist element which arrives will be first removed 
	 */
	public FIFO() {
		// Needed for the RemovalStrategyRegistry
	}
	
	private FIFO(Map<Object, CacheEntry> cacheStore) {
		this.cacheStore = cacheStore;
		this.queue = new LinkedList<>();
	}
	
	@Override
	public String getName() {
		return "FIFO";
	}

	@Override
	public void notifyNew(Object key, CacheEntry value) {
		//Insert as last Element in the queue
		this.queue.offer(key);
	}

	@Override
	public void notifyAccess(Object key, CacheEntry value) {
		//Nothing to do
	}

	@Override
	public CacheEntry removeNext() {
		Object key = this.queue.remove();
		CacheEntry removedEntry = this.cacheStore.get(key);
		this.cacheStore.remove(key);
		return removedEntry;
	}

	@Override
	public void clear() {
		this.queue.clear();
		this.cacheStore.clear();
	}

	@Override
	public IRemovalStrategy createInstance(Map<Object, CacheEntry> cacheStore) {
		return new FIFO(cacheStore);
	}



}
