package de.uniol.inf.is.odysseus.dbenrich.cache;

public interface ICache<K,V> {
	V get(K key);
	
	void debug(); // TODO REMOVE
	
	void open();
	
	void close();
}
