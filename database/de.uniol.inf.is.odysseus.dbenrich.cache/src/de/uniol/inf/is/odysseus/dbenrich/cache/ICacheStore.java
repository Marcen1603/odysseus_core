package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.util.Map;

/**
 * This interface describes, how cache store can be accessed.
 *
 * @param <K> the type of the keys
 * @param <V> the type of the values
 */
public interface ICacheStore<K,V> extends Map<K,V> { }
