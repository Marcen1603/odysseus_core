package de.uniol.inf.is.odysseus.cache;

import java.util.Map;

/**
 * This interface describes, how cache stroes can be accessed
 * @param <K> the keys
 * @param <V> the values
 */
public interface ICacheStore<K,V> extends Map<K,V> {

}
