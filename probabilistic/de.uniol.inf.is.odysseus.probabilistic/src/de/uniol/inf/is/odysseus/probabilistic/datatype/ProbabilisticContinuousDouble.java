package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Pair;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticContinuousDouble implements Serializable, Cloneable {
    /**
	 * 
	 */
    private static final long                          serialVersionUID = 5858308006884394418L;
    private final Pair<Pair<Double, Double>, Double>[] values;

    public ProbabilisticContinuousDouble(final double mean, double sigma, double probability) {
        this(new Pair<Double, Double>(mean, sigma), probability);
    }

    public ProbabilisticContinuousDouble(final Pair<Double, Double> distribution, double probability) {
        this(new Pair<Pair<Double, Double>, Double>(distribution, probability));
    }

    public ProbabilisticContinuousDouble(final Pair<Pair<Double, Double>, Double> value) {
        this(new Pair[] { value });
    }

    public ProbabilisticContinuousDouble(final Pair<Pair<Double, Double>, Double>[] values) {
        this.values = values;
    }

    public ProbabilisticContinuousDouble(final Pair<Double, Double>[] values, final Double[] probabilities) {
        final int length = Math.min(values.length, probabilities.length);
        this.values = new Pair[length];
        for (int i = 0; i < length; i++) {
            this.values[i] = new Pair<Pair<Double, Double>, Double>(values[i], probabilities[i]);
        }
    }

    @Override
    public ProbabilisticContinuousDouble clone() {
        return new ProbabilisticContinuousDouble(this.values.clone());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Pair<Pair<Double, Double>, Double> value : values) {
            if (sb.length() > 1) {
                sb.append(";");
            }
            sb.append("𝒩(").append(value.getE1().getE1()).append(",").append(value.getE1().getE2()).append("):")
                    .append(value.getE2());
        }
        sb.append(")");
        return sb.toString();
    }

    public Pair<Pair<Double, Double>, Double>[] values() {
        return values;
    }
}
