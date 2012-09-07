package de.uniol.inf.is.odysseus.dbenrich.cache;

public interface IRemovalStrategy<K, V extends CacheEntry<?>> {

	/**
	 * Notifies the removal strategy, that a new key-value-pair was
	 * added to the cache.
	 */
	void handleNewEntry(K key, V value);

	/**
	 * Removes the cache entry, that is to be removed next by the order
	 * of the specific removal strategy, from the cache as well as the
	 * removal strategy if present.
	 */
	void removeNext();
	
	//TODO createInstance(params)
}
