package de.uniol.inf.is.odysseus.probabilistic.math;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class IntervalProbabilityAssignment implements Cloneable, Map<Interval, Double>, Iterable<Interval> {
    private final Map<Interval, Double> claims = new HashMap<Interval, Double>();

    /**
    *
    */
    public IntervalProbabilityAssignment() {
    }

    /**
     * @param evidence
     */
    public IntervalProbabilityAssignment(final Interval evidence) {
        this.claims.put(evidence, 1.0);
    }

    public double getBelief(final Interval evidence) {
        double belief = 0.0;
        for (final Map.Entry<Interval, Double> entry : this.claims.entrySet()) {
            if (evidence.contains(entry.getKey())) {
                belief += entry.getValue();
            }
        }
        return belief;
    }

    public double getPlausibility(final Interval evidence) {
        double plausibility = 0.0;
        for (final Map.Entry<Interval, Double> entry : this.claims.entrySet()) {
            if (!evidence.intersection(entry.getKey()).isEmpty()) {
                plausibility += entry.getValue();
            }
        }
        return plausibility;
    }

    public double getCommonality(final Interval evidence) {
        double commonality = 0.0;
        for (final Map.Entry<Interval, Double> entry : this.claims.entrySet()) {
            if (entry.getKey().contains(evidence)) {
                commonality += entry.getValue();
            }
        }
        return commonality;
    }

    public IntervalProbabilityAssignment combine(final IntervalProbabilityAssignment other) {
        final IntervalProbabilityAssignment combination = new IntervalProbabilityAssignment();
        for (final Map.Entry<Interval, Double> entry : this.claims.entrySet()) {
            for (final Interval evidence : other) {
                final Interval combinedEvidence = entry.getKey().intersection(evidence);
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
    public Double getProbability(final Interval evidence) {
        return this.claims.get(evidence) == null ? 0.0 : this.claims.get(evidence);

    }

    public Double getTotalProbability() {
        double probability = 0.0;
        for (final Interval evidence : this) {
            probability += this.getProbability(evidence);
        }
        return probability;
    }

    public void add(final Interval evidence, final double probability) {
        if ((!evidence.isEmpty()) && (!Double.isNaN(probability))) {
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
            final IntervalProbabilityAssignment cloned = this.clone();
            this.claims.clear();

            for (final Map.Entry<Interval, Double> entry : cloned.claims.entrySet()) {
                this.claims.put(entry.getKey(), entry.getValue() / sum);
            }
        }
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public IntervalProbabilityAssignment clone() {
        final IntervalProbabilityAssignment clone = new IntervalProbabilityAssignment();
        for (final Map.Entry<Interval, Double> entry : this.claims.entrySet()) {
            clone.claims.put(entry.getKey().clone(), entry.getValue());
        }
        return clone;
    }

    @Override
    public Iterator<Interval> iterator() {
        return this.claims.keySet().iterator();
    }

    @Override
    public void clear() {
        this.claims.clear();

    }

    @Override
    public boolean containsKey(final Object key) {
        return this.claims.containsKey(key);
    }

    @Override
    public boolean containsValue(final Object value) {
        return this.claims.containsValue(value);
    }

    @Override
    public Set<java.util.Map.Entry<Interval, Double>> entrySet() {
        return this.claims.entrySet();
    }

    @Override
    public Double get(final Object key) {
        return this.claims.get(key);
    }

    @Override
    public boolean isEmpty() {
        return this.claims.isEmpty();
    }

    @Override
    public Set<Interval> keySet() {
        return this.claims.keySet();
    }

    @Override
    public Double put(final Interval evidence, final Double probability) {
        return this.claims.put(evidence, probability);
    }

    @Override
    public void putAll(final Map<? extends Interval, ? extends Double> intervalProbabilityAssignment) {
        this.claims.putAll(intervalProbabilityAssignment);

    }

    @Override
    public Double remove(final Object key) {
        return this.claims.remove(key);
    }

    @Override
    public int size() {
        return this.claims.size();
    }

    @Override
    public Collection<Double> values() {
        return this.claims.values();
    }

}
