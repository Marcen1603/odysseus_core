package de.uniol.inf.is.odysseus.dbenrich.cache;

import de.uniol.inf.is.odysseus.dbenrich.cache.removalStrategy.IRemovalStrategy;


public class ReadOnlyCache<K, V> implements IReadOnlyCache<K, V> {

	private final ICacheStore<K, CacheEntry<V>> cacheStore;
	private final int maxSize;
	private final long expirationTime;

	private final IRetrievalStrategy<K, V> retrievalStrategy;
	private final IRemovalStrategy removalStrategy;

	public ReadOnlyCache(ICacheStore<K, CacheEntry<V>> cacheStore,
			IRetrievalStrategy<K, V> retrievalStrategy,
			IRemovalStrategy removalStrategy, int maxSize,
			long expirationTime) {
		this.cacheStore = cacheStore;
		this.retrievalStrategy = retrievalStrategy;
		this.removalStrategy = removalStrategy;
		if (maxSize <= 0)
			throw new IllegalArgumentException(maxSize + "(maxSize) <= 0");
		this.maxSize = maxSize;
		this.expirationTime = expirationTime;
	}

	@Override
	public V get(K key) {
		V data;

		// Try to get the value from the cache store
		CacheEntry<V> cacheEntry = cacheStore.get(key);

		// If it's not found or invalid, get it from caching strategy
		if (cacheEntry == null || cacheEntry.isExpired(expirationTime)) {
			System.out.println("No (valid) cacheEntry for key "
					+ key.toString() + ", Hash: " + key.hashCode());
			data = getInternal(key);
			putInternal(key, new CacheEntry<V>(data));
		} else {
			data = cacheEntry.accessData();
			removalStrategy.notifyAccess(key, cacheEntry);
		}

		return data;
	}

	private V getInternal(K key) {
		return retrievalStrategy.get(key);
	}

	private void putInternal(K key, CacheEntry<V> cacheEntry) {
		while(cacheStore.size() >= maxSize) {
			System.out.println("Removed a cache entry");
			removalStrategy.removeNext();
		}
		cacheStore.put(key, cacheEntry);
		removalStrategy.notifyNew(key, cacheEntry);
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
		// Clears the cache
		removalStrategy.clear();
	}
}
