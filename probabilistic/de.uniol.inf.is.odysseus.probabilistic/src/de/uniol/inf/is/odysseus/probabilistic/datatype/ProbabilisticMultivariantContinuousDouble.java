package de.uniol.inf.is.odysseus.probabilistic.datatype;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class ProbabilisticMultivariantContinuousDouble {
    private final Pair<ProbabilisticContinuousDouble, Double>[] mixtures;

    public ProbabilisticMultivariantContinuousDouble(final double mean, byte covarianceId, int covarianceIndex,
            double probability) {
        this(new ProbabilisticContinuousDouble(mean, covarianceId, covarianceIndex), probability);
    }

    public ProbabilisticMultivariantContinuousDouble(final ProbabilisticContinuousDouble continuousDouble,
            double probability) {
        this.mixtures = new Pair[] { new Pair<ProbabilisticContinuousDouble, Double>(continuousDouble, probability) };
    }

    public ProbabilisticMultivariantContinuousDouble(final Double[] means,
            final Pair<Byte, Integer>[] covarianceMatrices, final Double[] probabilities) {
        final int length = Math.min(covarianceMatrices.length, Math.min(means.length, probabilities.length));
        this.mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            this.mixtures[i] = new Pair<ProbabilisticContinuousDouble, Double>(new ProbabilisticContinuousDouble(
                    means[i], covarianceMatrices[i]), probabilities[i]);
        }
    }

    public ProbabilisticMultivariantContinuousDouble(Pair<ProbabilisticContinuousDouble, Double>[] mixtures) {
        this.mixtures = mixtures;
    }

    public ProbabilisticMultivariantContinuousDouble(
            ProbabilisticMultivariantContinuousDouble probabilisticMultivariantContinuousDouble) {
        this.mixtures = probabilisticMultivariantContinuousDouble.mixtures.clone();
    }

    public Pair<ProbabilisticContinuousDouble, Double>[] getMixtures() {
        return mixtures;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Pair<ProbabilisticContinuousDouble, Double> mixture : getMixtures()) {
            if (sb.length() > 1) {
                sb.append(";");
            }
            sb.append(mixture.getE1().toString()).append(":").append(mixture.getE2());
        }
        sb.append(")");
        return sb.toString();
    }
}
