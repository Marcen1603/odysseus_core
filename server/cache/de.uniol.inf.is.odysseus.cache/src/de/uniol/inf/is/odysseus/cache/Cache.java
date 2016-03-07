package de.uniol.inf.is.odysseus.cache;

import java.util.List;

import de.uniol.inf.is.odysseus.cache.removalstrategy.IRemovalStrategy;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.cache.ICache;

public class Cache implements ICache {
	
	/**
	 * The implementation of the cache store
	 */
	private final ICacheStore<Object, CacheEntry> cacheStore;
	
	/**
	 * The implementation of the removal strategy
	 */
	private final IRemovalStrategy removalStrategy;
	
	/**
	 * The expiration time of caching records
	 */
	private final long expirationTime;
	
	/**
	 * The maximum number of tuples in the cache
	 */
	private final int maxSize;
	
	/**
	 * The actually number of tuples which are hold in the cache
	 */
	private int actualSize;
	
	private int cachehit;
	
	private int cachemiss;
	
	private int cacheinsert;
	
	private int cacheremove;
	/**
	 * Constructor for the ReadOnlyCache. This means, Caching Records are not modified, only read
	 * If a record can not be found by the specified key or the record isn�t valid, null we be returned.
	 * @param cacheStore the cache store
	 * @param removalStrategy the removal strategy
	 * @param time the expiration time
	 * @param size the number of maximum tuples which will be hold in the cache
	 */
	public Cache(ICacheStore<Object, CacheEntry> cacheStore, IRemovalStrategy removalStrategy, 
						long time, int size) {
		this.cacheStore = cacheStore;
		this.removalStrategy = removalStrategy;
		
		if(size <= 0 ) {
			throw new IllegalArgumentException("maxSize must be > 0");
		}
		if(time <= 0)
		{
			throw new IllegalArgumentException("expiration Time must be > 0");
		}
		this.expirationTime = time;
		this.maxSize = size;
		this.actualSize = 0;
		this.cachehit = 0;
		this.cachemiss = 0;
		this.cacheinsert = 0;
		this.cacheremove = 0;
	}
	
	@Override
	public List<IStreamObject<?>> get(Object key) {
		List<IStreamObject<?>> tuple;
		CacheEntry cacheEntry = this.cacheStore.get(key);
		if(cacheEntry == null || cacheEntry.isExpired(this.expirationTime)) {
			this.cachemiss++;
			return null;
		} else {
			this.cachehit++;
			tuple = cacheEntry.accesData();
			this.removalStrategy.notifyAccess(key, cacheEntry);
		}
		return tuple;
	}

	@Override
	public void put(Object key, List<IStreamObject<?>> value) {
		CacheEntry cacheEntry = new CacheEntry(key, value);
		while(this.cacheStore.size() >= this.maxSize && this.actualSize >= this.maxSize) {
			this.cacheremove++;
			CacheEntry removedEntry = this.removalStrategy.removeNext();
			this.actualSize = this.actualSize - removedEntry.getData().size();
		}
		this.cacheStore.put(key, cacheEntry);
		this.removalStrategy.notifyNew(key, cacheEntry);
		this.cacheinsert++;
		this.actualSize = this.actualSize + value.size();
	}

	@Override
	public void close() {
		this.removalStrategy.clear();
	}
	
	/**
	 * @return the maximum number of tuples which are hold in the cache
	 * Note: this value is the count of ALL Tuples of the cache and
	 * not the count of the number of cache entries.
	 */
	public int getMaxSize() {
		return this.maxSize;
	}
	
	/**
	 * @return the actually number of tuples which are hold in the cache.
	 * Note: this value represents the number of all tuples in the cache.
	 * If a cache entry is added to the cache this value will be increased
	 * by the number of tuples the cache entry holds
	 */
	public int getActualSize() {
		return this.actualSize;
	}
	
	/**
	 * @return number of cache hits
	 */
	@Override
	public int getCacheHits() {
		return this.cachehit;
	}
	
	/**
	 * @return number of cache misses
	 */
	@Override
	public int getCacheMiss() {
		return this.cachemiss;
	}
	
	/**
	 * @return number of cache inserts
	 */
	@Override
	public int getCacheInsert() {
		return this.cacheinsert;
	}
	
	/**
	 * @return number of cache removes
	 */
	@Override
	public int getCacheRemoves() {
		return this.cacheremove;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" (");
		sb.append("cache store: ").append(cacheStore).append(", ");
		sb.append("actual size: ").append(actualSize).append(", ");
		sb.append("max size: ").append(maxSize).append(", ");
		sb.append("cache hit: ").append(cachehit).append(", ");
		sb.append("cache insert: ").append(cacheinsert).append(", ");
		sb.append("cache miss: ").append(cachemiss).append(", ");
		sb.append("cache remove: ").append(cacheremove).append(", ");
		sb.append("expiration time: ").append(expirationTime).append(", ");
		sb.append("removal strategy: ").append(removalStrategy);
		sb.append(")");
		return sb.toString();
	}
}
