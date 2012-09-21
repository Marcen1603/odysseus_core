package de.uniol.inf.is.odysseus.dbenrich.cache;

/**
 * This class represents a cache entry. It contains a data element as well as 
 * metadata.
 * 
 * Note, that the metadata is initially set on creation and 
 * only updated, if the data is accessed via a accessData() call.
 * @param <V> The type of the data, that is cached
 */
public class CacheEntry<V> {

	private V data;
	private long insertationTime;
	private long lastAccessTime;
	private long accessCount;

	public CacheEntry(V data) {
		this.data = data;
		insertationTime = lastAccessTime = System.currentTimeMillis();
		accessCount = 0;
	}

	/**
	 * Returns the data element and takes care of the metadata updates.
	 * @return the data element
	 */
	public V accessData() {
		lastAccessTime = System.currentTimeMillis();
		accessCount++;
		return data;
	}

	/**
	 * Checks, if the cache entry is expired. An entry is expired, if it has 
	 * been in the cache for the given maximum period of time or longer.
	 * @param maxPeriod the maximum period of time for an entry to be valid 
	 * in the cache in Milliseconds
	 * @return true if expired, false if still valid
	 */
	public boolean isExpired(long maxPeriod) {
		return System.currentTimeMillis() - insertationTime >= maxPeriod;
	}
	
	public long getAccessCount() {
		return accessCount;
	}

	public V getData() {
		return data;
	}

	public long getInsertationTime() {
		return insertationTime;
	}

	public long getLastAccessTime() {
		return lastAccessTime;
	}
}
