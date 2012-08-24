package de.uniol.inf.is.odysseus.probabilistic.datatype;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class ProbabilisticMultivariateContinuousDouble {
    private final Pair<ProbabilisticContinuousDouble, Double>[] mixtures;

    public ProbabilisticMultivariateContinuousDouble(final double mean, final byte covarianceId,
            final int covarianceIndex, final double probability) {
        this(new ProbabilisticContinuousDouble(mean, covarianceId, covarianceIndex), probability);
    }

    public ProbabilisticMultivariateContinuousDouble(final ProbabilisticContinuousDouble continuousDouble,
            final double probability) {
        this.mixtures = new Pair[] { new Pair<ProbabilisticContinuousDouble, Double>(continuousDouble, probability) };
    }

    public ProbabilisticMultivariateContinuousDouble(final Double[] means,
            final Pair<Byte, Integer>[] covarianceMatrices, final Double[] probabilities) {
        final int length = Math.min(covarianceMatrices.length, Math.min(means.length, probabilities.length));
        this.mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            this.mixtures[i] = new Pair<ProbabilisticContinuousDouble, Double>(new ProbabilisticContinuousDouble(
                    means[i], covarianceMatrices[i]), probabilities[i]);
        }
    }

    public ProbabilisticMultivariateContinuousDouble(final Pair<ProbabilisticContinuousDouble, Double>[] mixtures) {
        this.mixtures = mixtures;
    }

    public ProbabilisticMultivariateContinuousDouble(
            final ProbabilisticMultivariateContinuousDouble probabilisticMultivariantContinuousDouble) {
        this.mixtures = probabilisticMultivariantContinuousDouble.mixtures.clone();
    }

    public Pair<ProbabilisticContinuousDouble, Double>[] getMixtures() {
        return this.mixtures;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (final Pair<ProbabilisticContinuousDouble, Double> mixture : this.getMixtures()) {
            if (sb.length() > 1) {
                sb.append(";");
            }
            sb.append(mixture.getE1().toString()).append(":").append(mixture.getE2());
        }
        sb.append(")");
        return sb.toString();
    }
}
