package de.uniol.inf.is.odysseus.dbenrich.cache;

import java.util.HashMap;
import java.util.Map;


/**
 * This class implements ICacheStore using a data structure, that is
 * located in the working memory.
 */
/*
 * Note: Although using a composition is the preferred method to use a
 * map in general, there would be no gain from that indirection here. If
 * this class gets extended in the future, it can be easily modified to use
 * a composition instead.
 */
public class WorkingMemoryCacheStore<K, V> extends HashMap<K, V> implements
		ICacheStore<K, V> {

	private static final long serialVersionUID = -5431984273728785283L;

	/**
	 * @see HashMap#HashMap()
	 */
	public WorkingMemoryCacheStore() {
		super();
	}

	/**
	 * @see HashMap#HashMap(int, float)
	 */
	public WorkingMemoryCacheStore(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * @see HashMap#HashMap(int)
	 */
	public WorkingMemoryCacheStore(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * @see HashMap#HashMap(Map)
	 */
	public WorkingMemoryCacheStore(Map<? extends K, ? extends V> m) {
		super(m);
	}
}
