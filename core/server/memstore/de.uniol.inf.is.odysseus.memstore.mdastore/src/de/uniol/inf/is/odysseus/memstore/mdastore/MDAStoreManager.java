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
	private static final Map<String, MDAStore> stores = Maps
			.newHashMap();

	/**
	 * Creates a new MDAStore.
	 * 
	 * @param name
	 *            The name for the MDAStore.
	 */
	public static MDAStore create(String name) {
		MDAStore store = new MDAStore();
		stores.put(name, store);
		return store;
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
	public static MDAStore get(String name) {
		if (exists(name)) {
			return (MDAStore) stores.get(name);
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