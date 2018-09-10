package de.uniol.inf.is.odysseus.cache.removalstrategy;

import java.util.Map;
import de.uniol.inf.is.odysseus.cache.CacheEntry;

public interface IRemovalStrategy {
	
	/**
	 * @return the name of the removal strategy
	 */
	String getName();
	
	/**
	 * notifies that a new cache entry was added
	 * @param key the key 
	 * @param value the value matching to the key
	 */
	void notifyNew(Object key, CacheEntry value);
	
	/**
	 * notifies that a caching entry was accessed
	 * @param key the key
	 * @param value the value matching to the key
	 */
	void notifyAccess(Object key, CacheEntry value);
	
	/**
	 * removes the next caching entry with the specified 
	 * removal strategy
	 * @return the removed cache entry
	 */
	CacheEntry removeNext();
	
	/**
	 * clears the cache
	 */
	void clear();
	
	/**
	 * creates an instance of the specified removal strategy
	 * @param cacheStore the cache store
	 * @return an instance of the removal strategy
	 */
	IRemovalStrategy createInstance(Map<Object, CacheEntry> cacheStore);

}
