package de.uniol.inf.is.odysseus.cache;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * Represents a Cache Entry
 * A Cache Entry contains n Objects of IStreamObject in a ArrayList
 * A Cache Entry contains its key with which it is referenced and Metadata
 * for the caching strategy
 * Metadata will bei automatically when the cache entry is initialized
 * Metadata are: 
 * insertionTime: the time the cache entry is created and inserted in the cache
 * lastAccesTime: the last time the cache entry was accessed through the cache
 * accessCount: the count the cache entry was accessed throug the cache
 * All metadata will be refreshed when accessing the cache entry
 * @author Daniel
 *
 */
public class CacheEntry {
	
	/**
	 * The ArrayList of IStreamObjects
	 */
	private List<IStreamObject<?>> tuples;
	
	/**
	 * The reference as a key the cache entry can be found in the cache
	 */
	private final Object key;
	
	/**
	 * The creation time of the cache entry
	 */
	private final long insertionTime;
	
	/**
	 * The time the cache entry was last accessed through the cache
	 */
	private long lastAccessTime;
	
	/**
	 * The count of accesses of the cache entry throug the cache
	 */
	private int accessCount;
	
	/**
	 * Constructor for the Cache entry
	 * @param key the key to reference this cache entry in the cache
	 * @param tuples the List of IStreamObjects which this entry holds
	 */
	public CacheEntry(Object key, List<IStreamObject<?>> tuples) {
		this.key = key;
		this.tuples = tuples;
		this.insertionTime = this.lastAccessTime = System.currentTimeMillis();
		this.accessCount = 0;
	}
	
	/**
	 * This method refreshs the metadata when this entry was accessed through the cache
	 * @return the ArrayList of IStreamObjects, no metadata 
	 */
	public List<IStreamObject<?>> accesData() {
		this.lastAccessTime = System.currentTimeMillis();
		this.accessCount++;
		return tuples;
	}
	
	/**
	 * checks whether the cache entry is expired
	 * A cache entry is valid if the actually time - insertionTime is
	 * smaller then maxPeriod 
	 * @param maxPeriod the time a cache entry is valid
	 * @return true if the cache entry is valid, false then
	 */
	public boolean isExpired(long maxPeriod) {
		return System.currentTimeMillis() - insertionTime >= maxPeriod;
	}
	
	/**
	 * @return the key referencing this cache entry in the cache
	 */
	public Object getKey() {
		return this.key;
	}
	
	/**
	 * @return the data of this cache entry
	 */
	public List<IStreamObject<?>> getData() {
		return this.tuples;
	}
	/**
	 * @return the last access time of the cache entry
	 */
	public long getLastAccesTime() {
		return this.lastAccessTime;
	}
	
	/**
	 * @return the insertion time of this cache entry
	 */
	public long getInsertionTime() {
		return this.insertionTime;
	}
	
	/**
	 * @return the access count of this cache entry
	 */
	public int getAccessCount() {
		return this.accessCount;
	}

}
