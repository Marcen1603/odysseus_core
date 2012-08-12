package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.util.HashMap;

public class SomeCache<K, V> implements ICache<K, V> {
	
	private HashMap<K,V> store = new HashMap<K, V>();
	
	private IRetrievalStrategy<K, V> retrievalStrategy;
	
	// + replacement strategy

	public SomeCache(IRetrievalStrategy<K, V> retrievalStrategy) {
		this.retrievalStrategy = retrievalStrategy;
	}

	@Override
	public V get(K key) {
		// wie prefetching mit rein? ab und zu bei get, weil thread zu unpassend?
		// copy liefern?
		
		// Try to get the value from the cache store
		V value = store.get(key);
		
		// If it's not found or invalid, get it from caching strategy
		if(value==null) { // || isInvalid(value)) {
			System.out.println("No value for key " + key.toString() + ", Hash: " + key.hashCode());
			value = getInternal(key);
			store.put(key, value);
		}
		
		return value;
	}

	private V getInternal(K key) {
		return retrievalStrategy.get(key);
	}
	
//	void put(K key, V value) { }
	
//	void remove() { }

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
		retrievalStrategy = null;
		store = null;
	}
}
