package de.uniol.inf.is.odysseus.probabilistic.datatype;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class ProbabilisticContinuousDouble {
    private final Pair<NormalDistribution, Double>[] mixtures;

    public ProbabilisticContinuousDouble(final double mean, final byte covarianceId,
            final int covarianceIndex, final double probability) {
        this(new NormalDistribution(mean, covarianceId, covarianceIndex), probability);
    }

    public ProbabilisticContinuousDouble(final NormalDistribution distribution,
            final double probability) {
        this.mixtures = new Pair[] { new Pair<NormalDistribution, Double>(distribution, probability) };
    }

    public ProbabilisticContinuousDouble(final Double[] means,
            final Pair<Byte, Integer>[] covarianceMatrices, final Double[] probabilities) {
        final int length = Math.min(covarianceMatrices.length, Math.min(means.length, probabilities.length));
        this.mixtures = new Pair[length];
        for (int i = 0; i < length; i++) {
            this.mixtures[i] = new Pair<NormalDistribution, Double>(new NormalDistribution(
                    means[i], covarianceMatrices[i]), probabilities[i]);
        }
    }

    public ProbabilisticContinuousDouble(final Pair<NormalDistribution, Double>[] mixtures) {
        this.mixtures = mixtures;
    }

    public ProbabilisticContinuousDouble(
            final ProbabilisticContinuousDouble probabilisticContinuousDouble) {
        this.mixtures = probabilisticContinuousDouble.mixtures.clone();
    }

    public Pair<NormalDistribution, Double>[] getMixtures() {
        return this.mixtures;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (final Pair<NormalDistribution, Double> mixture : this.getMixtures()) {
            if (sb.length() > 1) {
                sb.append(";");
            }
            sb.append(mixture.getE1().toString()).append(":").append(mixture.getE2());
        }
        sb.append(")");
        return sb.toString();
    }
}
