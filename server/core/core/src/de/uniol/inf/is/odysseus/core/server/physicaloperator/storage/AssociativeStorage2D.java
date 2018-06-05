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
public class AssociativeStorage2D implements IAssociativeStorage<Tuple<?>> {

    private Map<Object, Object> store = new HashMap<Object, Object>();
    private List<Integer> sizes;

    /**
     * @param sizes
     *            The size of the storage.
     */
    public AssociativeStorage2D(List<Integer> sizes) {
        Objects.requireNonNull(sizes);
        Preconditions.checkElementIndex(1, sizes.size());
        this.sizes = sizes;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized void set(Object[] path, int[] index, double value) {
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
            tmpStore.put(lastKey, new double[sizes.get(0)][sizes.get(1)]);
        }
        ((double[][]) tmpStore.get(lastKey))[index[0]][index[1]] = value;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized  double get(Object[] path, int[] position) {
        Map<Object, Object> tmpStore = this.store;
        for (int i = 0; i < path.length - 1; i++) {
            Object index = path[i];
            tmpStore = (Map<Object, Object>) tmpStore.get(index);
        }
        Object lastIndex = path[path.length - 1];
        return ((double[][]) tmpStore.get(lastIndex))[position[0]][position[1]];
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized double[] getLine(Object[] path, int[] position) {
        Map<Object, Object> tmpStore = this.store;
        for (int i = 0; i < path.length - 1; i++) {
            Object index = path[i];
            tmpStore = (Map<Object, Object>) tmpStore.get(index);
        }
        Object lastIndex = path[path.length - 1];
        return ((double[][]) tmpStore.get(lastIndex))[position[0]];
    }

}
