package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Access to different MDAStores identified by their names.
 * 
 * @author Michael Brand
 *
 */
public class MDAStoreManager {

	/**
	 * All created stores mapped to their names.
	 */
	private static final Map<String, MDAStore<? extends Comparable<?>>> stores = Maps
			.newHashMap();

	/**
	 * Creates a new MDAStore.
	 * 
	 * @param name
	 *            The name for the MDAStore.
	 */
	public static <T extends Comparable<? super T>> void create(String name) {
		stores.put(name, new MDAStore<T>());
	}

	/**
	 * Removes a MDAStore.
	 * 
	 * @param storeName
	 *            The name of the MDAStore.
	 */
	public static void remove(String storeName) {
		if (exists(storeName)) {
			stores.remove(storeName);
		}
	}

	/**
	 * Gets a MDAStore.
	 * 
	 * @param name
	 *            The name of the MDAStore.
	 * @return The MDASTore identified by <code>name</code> or null, if there is
	 *         no MDAStore with that name.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> MDAStore<T> get(String name) {
		if (exists(name)) {
			return (MDAStore<T>) stores.get(name);
		}
		return null;
	}

	/**
	 * Checks, if a MDAStore exists.
	 * 
	 * @param name
	 *            The name of the MDAStore.
	 * @return True, if a MDAStore with that name exists.
	 */
	public static boolean exists(String name) {
		return stores.containsKey(name);
	}

}