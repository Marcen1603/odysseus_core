package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.util.Map;


public class ReadOnlyCache<K, V> implements IReadOnlyCache<K, V> {

	private final Map<K,V> cacheStore;
	private final int maxSize;

	private final IRetrievalStrategy<K, V> retrievalStrategy;
	private final IRemovalStrategy<K, V> removalStrategy;

	public ReadOnlyCache(Map<K,V> cacheStore, IRetrievalStrategy<K, V> retrievalStrategy, IRemovalStrategy<K, V> removalStrategy, int maxSize) {
		this.cacheStore = cacheStore;
		this.retrievalStrategy = retrievalStrategy;
		this.removalStrategy = removalStrategy;
		if(maxSize <= 0) throw new IllegalArgumentException(
				maxSize+"(maxSize) <= 0");
		this.maxSize = maxSize;
	}

	@Override
	public V get(K key) {
		// wie prefetching mit rein? ab und zu bei get, weil thread zu unpassend?

		// Try to get the value from the cache store
		V value = cacheStore.get(key);

		// If it's not found or invalid, get it from caching strategy
		if(value==null) { // || isExpired(value)) { //FIXME
			System.out.println("No value for key " + key.toString() + ", Hash: " + key.hashCode());
			value = getInternal(key);
			putInternal(key, value);
		}

		return value;
	}

	private V getInternal(K key) {
		return retrievalStrategy.get(key);
	}

	private void putInternal(K key, V value) {
		while(cacheStore.size() >= maxSize) {
			System.out.println("Removed a cache entry");
			removalStrategy.removeNext();
		}
		cacheStore.put(key, value);
		removalStrategy.handleNewEntry(key, value);
	}

	// TODO maybe purge, clear depending on further implementation

	@Override
	public void debug() {
		System.out.println("debugSomeCache");
	}

	@Override
	public void open() {
		retrievalStrategy.open();
	}

	@Override
	public void close() {
		retrievalStrategy.close();
	}
}
