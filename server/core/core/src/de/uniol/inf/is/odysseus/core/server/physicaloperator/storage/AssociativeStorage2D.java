/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AssociativeStorage2D implements IAssociativeStorage<Tuple<?>> {

    private Map<Object, Object> store = new HashMap<Object, Object>();
    private List<Integer> sizes;

    /**
     * @param hierarchy
     * @param attributes
     * @param sizes
     */
    public AssociativeStorage2D(int depth, List<Integer> sizes) {
        this.sizes = sizes;
    }

    @Override
	@SuppressWarnings("unchecked")
    public void set(Object[] path, int[] index, Object value) {
        Map<Object, Object> tmpStore = this.store;
        for (int i = 0; i < path.length - 1; i++) {
            Object key = path[i];
            if (!tmpStore.containsKey(key)) {
                tmpStore.put(key, new HashMap<Object, Object>());
            }
            tmpStore = (Map<Object, Object>) tmpStore.get(key);
        }
        Object lastKey = path[path.length - 1];
        if (tmpStore.get(lastKey) == null) {
            tmpStore.put(lastKey, new Object[sizes.get(0)][sizes.get(1)]);
        }
        ((Object[][]) tmpStore.get(lastKey))[index[0]][index[1]] = value;
    }

    @Override
	@SuppressWarnings("unchecked")
    public Object get(Object[] path, int[] position) {
        Map<Object, Object> tmpStore = this.store;
        for (int i = 0; i < path.length - 1; i++) {
            Object index = path[i];
            tmpStore = (Map<Object, Object>) tmpStore.get(index);
        }
        Object lastIndex = path[path.length - 1];
        return ((Object[][]) tmpStore.get(lastIndex))[position[0]][position[1]];
    }

}
