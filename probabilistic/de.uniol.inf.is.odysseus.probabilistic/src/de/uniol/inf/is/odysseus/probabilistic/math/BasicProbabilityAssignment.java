package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class BasicProbabilityAssignment<T extends Comparable<T>> implements Cloneable, Map<Evidence<T>, Double>,
        Iterable<Evidence<T>> {
    private final Map<Evidence<T>, Double> claims = new HashMap<Evidence<T>, Double>();

    /**
    *
    */
    public BasicProbabilityAssignment() {
    }

    /**
     * @param evidence
     */
    public BasicProbabilityAssignment(final Evidence<T> evidence) {
        this.claims.put(evidence, 1.0);
    }

    public double getBelief(final Evidence<T> evidence) {
        double belief = 0.0;
        for (final Map.Entry<Evidence<T>, Double> entry : this.claims.entrySet()) {
            if (evidence.isSuperSet(entry.getKey())) {
                belief += entry.getValue();
            }
        }
        return belief;
    }

    public double getPlausibility(final Evidence<T> evidence) {
        double plausibility = 0.0;
        for (final Map.Entry<Evidence<T>, Double> entry : this.claims.entrySet()) {
            if (!evidence.intersection(entry.getKey()).isEmpty()) {
                plausibility += entry.getValue();
            }
        }
        return plausibility;
    }

    public double getCommonality(final Evidence<T> evidence) {
        double commonality = 0.0;
        for (final Map.Entry<Evidence<T>, Double> entry : this.claims.entrySet()) {
            if (entry.getKey().isSuperSet(evidence)) {
                commonality += entry.getValue();
            }
        }
        return commonality;
    }

    public BasicProbabilityAssignment<T> combine(final BasicProbabilityAssignment<T> other) {
        final BasicProbabilityAssignment<T> combination = new BasicProbabilityAssignment<T>();
        for (final Map.Entry<Evidence<T>, Double> entry : this.claims.entrySet()) {
            for (final Evidence<T> evidence : other) {
                final Evidence<T> combinedEvidence = entry.getKey().intersection(evidence);
                if (!combinedEvidence.isEmpty()) {
                    double probability = entry.getValue() * other.getProbability(evidence);
                    final Double previous = combination.claims.get(combinedEvidence);
                    if (previous != null) {
                        probability += previous;
                    }
                    combination.claims.put(combinedEvidence, probability);
                }
            }
        }
        return combination;
    }

    public boolean isNormalized() {
        return this.isNormalized(0.0);
    }

    public boolean isNormalized(final double delta) {
        double sum = 0.0;
        for (final Double probability : this.claims.values()) {
            sum += probability;
        }
        return Math.abs(sum - 1.0) <= delta;
    }

    /**
     * @param evidence
     * @return
     */
    public Double getProbability(final Evidence<T> evidence) {
        return this.claims.get(evidence) == null ? 0.0 : this.claims.get(evidence);

    }

    public Double getTotalProbability() {
        double probability = 0.0;
        for (final Evidence<T> evidence : this) {
            probability += this.getProbability(evidence);
        }
        return probability;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Evidence<T>> iterator() {
        return this.claims.keySet().iterator();
    }

    public void add(final Evidence<T> evidence, final double probability) {
        if (!evidence.isEmpty() && !Double.isNaN(probability)) {
            final Double b = this.claims.get(evidence);
            this.claims.put(evidence, probability + (b != null ? b : 0.0));
        }
    }

    public void normalize() {
        double sum = 0.0;
        for (final Double probability : this.claims.values()) {
            sum += probability;
        }
        if (sum != 1.0) {
            final BasicProbabilityAssignment<T> cloned = this.clone();
            this.claims.clear();

            for (final Map.Entry<Evidence<T>, Double> entry : cloned.claims.entrySet()) {
                this.claims.put(entry.getKey(), entry.getValue() / sum);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public BasicProbabilityAssignment<T> clone() {
        final BasicProbabilityAssignment<T> clone = new BasicProbabilityAssignment<T>();
        for (final Map.Entry<Evidence<T>, Double> entry : this.claims.entrySet()) {
            clone.claims.put(entry.getKey().clone(), entry.getValue());
        }
        return clone;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.claims.toString();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#clear()
     */
    @Override
    public void clear() {
        this.claims.clear();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    @Override
    public boolean containsKey(final Object key) {
        return this.claims.containsKey(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    @Override
    public boolean containsValue(final Object value) {
        return this.claims.containsValue(value);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    @Override
    public Set<java.util.Map.Entry<Evidence<T>, Double>> entrySet() {
        return this.claims.entrySet();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    @Override
    public Double get(final Object key) {
        return this.claims.get(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return this.claims.isEmpty();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    @Override
    public Set<Evidence<T>> keySet() {
        return this.claims.keySet();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public Double put(final Evidence<T> evidence, final Double probability) {
        return this.claims.put(evidence, probability);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    @Override
    public void putAll(final Map<? extends Evidence<T>, ? extends Double> basicProbabilityAssignment) {
        this.claims.putAll(basicProbabilityAssignment);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#remove(java.lang.Object)
     */
    @Override
    public Double remove(final Object key) {
        return this.claims.remove(key);
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#size()
     */
    @Override
    public int size() {
        return this.claims.size();
    }

    /*
     * (non-Javadoc)
     * @see java.util.Map#values()
     */
    @Override
    public Collection<Double> values() {
        return this.claims.values();
    }
}
