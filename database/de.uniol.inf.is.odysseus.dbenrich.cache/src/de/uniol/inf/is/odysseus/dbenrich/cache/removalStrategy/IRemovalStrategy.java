package de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy;

import java.util.Map;

import de.uniol.inf.is.odysseus.dbenrich.cache.CacheEntry;

public interface IRemovalStrategy {

	/**
	 * Returns the name of the strategy.
	 * 
	 * @return the name
	 */
	String getName();

	/**
	 * Notifies the removal strategy, that a new key-value-pair was added to the
	 * cache.
	 * @param key the key
	 * @param value the value
	 */
	void notifyNew(Object key, CacheEntry<?> value);
	
	/**
	 * Notifies the removal strategy, that a key-value-pair was accessed.
	 * @param key the key
	 * @param value the value
	 */
	void notifyAccess(Object key, CacheEntry<?> value);

	/**
	 * Removes the cache entry, that is to be removed next by the order of the
	 * specific removal strategy, from the cache as well as the removal strategy
	 * if present.
	 */
	void removeNext();

	/**
	 * Used instead of a public constructor.
	 * 
	 * @param cacheStore
	 *            the cache store to use
	 * @return a new object, that implements IRemovalStrategy
	 */
	IRemovalStrategy createInstance(Map<?, CacheEntry<?>> cacheStore);
}
