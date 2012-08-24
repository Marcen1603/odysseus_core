package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class MultivariateCovarianceMatrix implements Serializable, Cloneable, Map<Byte, CovarianceMatrix> {

    /**
     * 
     */
    private static final long                 serialVersionUID = -8544066842144295230L;
    private final Map<Byte, CovarianceMatrix> matrices;

    public MultivariateCovarianceMatrix(final int size) {
        this.matrices = new HashMap<Byte, CovarianceMatrix>(size);
    }

    public MultivariateCovarianceMatrix(final MultivariateCovarianceMatrix multivariateCovarianceMatrix) {
        this.matrices = new HashMap<Byte, CovarianceMatrix>(multivariateCovarianceMatrix.matrices);
    }

    @Override
    public void clear() {
        this.matrices.clear();
    }

    @Override
    public boolean containsKey(final Object key) {
        return this.matrices.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.matrices.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<Byte, CovarianceMatrix>> entrySet() {
        return this.matrices.entrySet();
    }

    @Override
    public CovarianceMatrix get(final Object key) {
        return this.matrices.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.matrices.isEmpty();
    }

    @Override
    public Set<Byte> keySet() {
        return this.matrices.keySet();
    }

    @Override
    public CovarianceMatrix put(final Byte key, final CovarianceMatrix value) {
        return this.matrices.put(key, value);
    }

    @Override
    public void putAll(final Map<? extends Byte, ? extends CovarianceMatrix> m) {
        this.matrices.putAll(m);
    }

    @Override
    public CovarianceMatrix remove(final Object key) {
        return this.matrices.remove(key);
    }

    @Override
    public int size() {
        return this.matrices.size();
    }

    @Override
    public Collection<CovarianceMatrix> values() {
        return this.matrices.values();
    }

    @Override
    public MultivariateCovarianceMatrix clone() {
        return new MultivariateCovarianceMatrix(this);
    }

}
