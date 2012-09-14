package de.uniol.inf.is.odysseus.dbenrich.cache;

/**
 * This interface describes, how read only caches can be accessed.
 * @param <K> the type of the keys
 * @param <V> the type of the values
 */
public interface IReadOnlyCache<K, V> {

	/**
	 * Returns the value, that is mapped to the given key in the cache.
	 * 
	 * @param key
	 *            the unique key
	 * @return the value which belongs to the key, or {@code null} if there is
	 *         no value
	 */
	public V get(K key);

	/**
	 * Initializes all required resources. Call this method before using the
	 * cache.
	 */
	public void open();

	/**
	 * Closes all unneeded resources. Call this method before using the cache.
	 */
	public void close();

	public IReadOnlyCache<K, V> clone() throws CloneNotSupportedException;
}
