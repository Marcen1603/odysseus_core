package de.uniol.inf.is.odysseus.cache.removalstrategy;

import java.util.Map;
import java.util.Set;
import de.uniol.inf.is.odysseus.cache.CacheEntry;

//note: based on a copy of de.uniol.inf.is.odysses.dbenrich.cache.Random
public class Random implements IRemovalStrategy {
	
	/**
	 * cache store for the management of the removal strategy
	 */
	private Map<Object, CacheEntry> cacheStore;
	
	/**
	 * The random object to generate a random integer
	 */
	private java.util.Random random;
	
	/**
	 * Constructor for the Random removal strategy.
	 * A random cache entry will be removed
	 */
	public Random() {
		//Needed for the RemovalStrategyRegistry
	}
	
	private Random(Map<Object, CacheEntry> cacheStore) {
		this.cacheStore = cacheStore;
		this.random = new java.util.Random();
		
	}
	
	@Override
	public String getName() {
		return "RANDOM";
	}

	@Override
	public void notifyNew(Object key, CacheEntry value) {
		//Nothing to do
	}

	@Override
	public void notifyAccess(Object key, CacheEntry value) {
		//Nothing to do
	}

	@Override
	public CacheEntry removeNext() {
		Set<Object> entries = this.cacheStore.keySet();
		int entryToRemove = this.random.nextInt(entries.size());
		int counter = 0;
		CacheEntry removedEntry;
		//Walk trough the Collection and find the Entry to remove. 
		//Inefficient if there are many entries
		for(Object key : entries) {
			if(counter == entryToRemove) {
				removedEntry = this.cacheStore.get(key);
				this.cacheStore.remove(key);
				return removedEntry;
			} else {
				counter++;
			}
		}
		return null;
	}

	@Override
	public void clear() {
		this.cacheStore.clear();
	}

	@Override
	public IRemovalStrategy createInstance(Map<Object, CacheEntry> cacheStore) {
		return new Random(cacheStore);
	}
}
