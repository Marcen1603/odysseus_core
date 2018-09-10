/**
 * 
 */
package de.uniol.inf.is.odysseus.core.physicaloperator.storage;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AssociativeStorageManager<T extends Tuple<?>> {
    private static final Map<String, IAssociativeStorage<? extends Tuple<?>>> stores = new HashMap<String, IAssociativeStorage<? extends Tuple<?>>>();

    private AssociativeStorageManager() {

    }

    public static <T extends Tuple<?>> void create(String name, IAssociativeStorage<T> store) {
        stores.put(name, store);
    }

    public static void remove(String storeName) {
        if (exists(storeName)) {
            stores.remove(storeName);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Tuple<?>> IAssociativeStorage<T> get(String name) {
        if (exists(name)) {
            return (IAssociativeStorage<T>) stores.get(name);
        }
        return null;
    }

    public static boolean exists(String name) {
        return stores.containsKey(name);
    }
}
