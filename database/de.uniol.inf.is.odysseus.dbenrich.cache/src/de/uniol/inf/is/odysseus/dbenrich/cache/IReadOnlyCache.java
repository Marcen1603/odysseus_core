package de.uniol.inf.is.odysseus.dbenrich.cache;

public interface IReadOnlyCache<K,V> {

	/**
	 * Returns the value, that is mapped to the given key in the cache.
	 * @param  key the unique key
	 * @return the value which belongs to the key, or {@code null}
	 * if there is no value
	 */
	V get(K key);

	void debug(); // TODO REMOVE

	/**
	 * Initializes all required resources. Call this method before using
	 * the cache.
	 */
	void open();

	/**
	 * Closes all unneeded resources. Call this method before using the cache.
	 */
	void close();
}
