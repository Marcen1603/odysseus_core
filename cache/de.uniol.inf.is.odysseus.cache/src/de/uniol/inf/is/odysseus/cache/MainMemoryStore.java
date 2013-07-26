package de.uniol.inf.is.odysseus.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for a cache which holds it´s elements in the main memory
 * @param <K> the Key
 * @param <V> the Value
 */
public class MainMemoryStore<K,V> extends HashMap<K,V> implements 
		ICacheStore<K,V> {
	
	/**
	 * For Serialization
	 */
	private static final long serialVersionUID = -6585248261169789525L;
	
	/**
	 * Default constructor
	 */
	public MainMemoryStore() {
		super();
	}
	
	/**
	 * Constructor with a initial Capacity an a loadFactor of the HashMap 
	 * @param initialCapacity the initial Capacity of this HashMap
	 * @param loadFactor the load Factor
	 */
	public MainMemoryStore(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	/**
	 * Constructor with a initial Capacity of the HashMap
	 * @param initialCapacity the initial Capacity of this HashMap
	 */
	public MainMemoryStore(int initialCapacity) {
		super(initialCapacity);
	}
	
	/**
	 * Constructor for a new HashMap with all Entries of m
	 * @param m the HashMap to copy
	 */
	public MainMemoryStore(Map<? extends K, ? extends V> m) {
		super(m);
	}

}
