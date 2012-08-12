package de.uniol.inf.is.odysseus.dbenrich.cache;

public interface IRetrievalStrategy<K,V> {
	V get(K key);
	
	void open();
	
	void close();
}
