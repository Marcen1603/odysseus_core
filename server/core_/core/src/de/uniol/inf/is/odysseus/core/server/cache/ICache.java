package de.uniol.inf.is.odysseus.core.server.cache;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public interface ICache {
	
	/**
	 * get the ArrayList of IStreamObjects from the cache
	 * @param key the referencing key
	 * @return the ArrayList of IStreamObjects. If no entry is 
	 * found with the given key, the method returns null
	 */
	public List<IStreamObject<?>> get(Object key);
	
	/**
	 * Puts A new ArrayList of IStreamObjects in the cache.
	 * Internal a new CacheEntry will be generated, with the
	 * given IStreamObjects
	 * @param key the key
	 * @param value the ArrayList of IStreamObjects
	 */
	public void put(Object key, List<IStreamObject<?>> value);
	
	/**
	 * Clears the cache
	 */
	public void close();
	
	public int getCacheHits();
	
	public int getCacheMiss(); 
	
	public int getCacheInsert(); 
	
	public int getCacheRemoves();
	
}
