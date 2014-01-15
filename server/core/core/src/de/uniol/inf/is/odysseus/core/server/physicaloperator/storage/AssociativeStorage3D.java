/**
 * 
 */
package de.uniol.inf.is.odysseus.core.server.physicaloperator.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.storage.IAssociativeStorage;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class AssociativeStorage3D implements IAssociativeStorage<Tuple<?>> {

    private Map<Object, Object> store = new HashMap<Object, Object>();
    private List<Integer> sizes;

    /**
     * @param sizes
     *            The size of the storage.
     */
    public AssociativeStorage3D(List<Integer> sizes) {
        Objects.requireNonNull(sizes);
        Preconditions.checkElementIndex(2, sizes.size());
        this.sizes = sizes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(Object[] path, int[] index, Double value) {
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
            tmpStore.put(lastKey, new Double[sizes.get(0)][sizes.get(1)][sizes.get(2)]);
        }
        ((Double[][][]) tmpStore.get(lastKey))[index[0]][index[1]][index[2]] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double get(Object[] path, int[] position) {
        Map<Object, Object> tmpStore = this.store;
        for (int i = 0; i < path.length - 1; i++) {
            Object index = path[i];
            tmpStore = (Map<Object, Object>) tmpStore.get(index);
        }
        Object lastIndex = path[path.length - 1];
        return ((Double[][][]) tmpStore.get(lastIndex))[position[0]][position[1]][position[2]];
    }

    @Override
    @SuppressWarnings("unchecked")
    public Double[] getLine(Object[] path, int[] position) {
        Map<Object, Object> tmpStore = this.store;
        for (int i = 0; i < path.length - 1; i++) {
            Object index = path[i];
            tmpStore = (Map<Object, Object>) tmpStore.get(index);
        }
        Object lastIndex = path[path.length - 1];
        return ((Double[][][]) tmpStore.get(lastIndex))[position[0]][position[1]];
    }
}
