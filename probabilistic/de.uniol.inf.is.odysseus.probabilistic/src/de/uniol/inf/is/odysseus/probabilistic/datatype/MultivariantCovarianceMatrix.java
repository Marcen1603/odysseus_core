package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MultivariantCovarianceMatrix implements Serializable, Cloneable, Map<Byte, CovarianceMatrix> {

    /**
     * 
     */
    private static final long           serialVersionUID = -8544066842144295230L;
    private Map<Byte, CovarianceMatrix> matrices;

    public MultivariantCovarianceMatrix(int size) {
        matrices = new HashMap<Byte, CovarianceMatrix>(size);
    }

    public MultivariantCovarianceMatrix(MultivariantCovarianceMatrix multivariantCovarianceMatrix) {
        this.matrices = new HashMap<Byte, CovarianceMatrix>(multivariantCovarianceMatrix.matrices);
    }

    @Override
    public void clear() {
        matrices.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        return matrices.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return matrices.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<Byte, CovarianceMatrix>> entrySet() {
        return matrices.entrySet();
    }

    @Override
    public CovarianceMatrix get(Object key) {
        return matrices.get(key);
    }

    @Override
    public boolean isEmpty() {
        return matrices.isEmpty();
    }

    @Override
    public Set<Byte> keySet() {
        return matrices.keySet();
    }

    @Override
    public CovarianceMatrix put(Byte key, CovarianceMatrix value) {
        return matrices.put(key, value);
    }

    @Override
    public void putAll(Map<? extends Byte, ? extends CovarianceMatrix> m) {
        matrices.putAll(m);
    }

    @Override
    public CovarianceMatrix remove(Object key) {
        return matrices.remove(key);
    }

    @Override
    public int size() {
        return matrices.size();
    }

    @Override
    public Collection<CovarianceMatrix> values() {
        return matrices.values();
    }

    @Override
    public MultivariantCovarianceMatrix clone() {
        return new MultivariantCovarianceMatrix(this);
    }

}
