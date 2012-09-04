package de.uniol.inf.is.odysseus.dbenrich.cache;

public interface IRetrievalStrategy<K,V> {

	/**
	 * Returns the value, that is mapped to the given key.
	 * @param  key the unique key
	 * @return the value which belongs to the key, or {@code null}
	 * if there is no value
	 */
	V get(K key);

	/**
	 * Initializes all required resources. Call this method before using the
	 * strategy.
	 */
	void open();

	/**
	 * Closes all unneeded resources. Call this method after using the
	 * strategy.
	 */
	void close();
}
