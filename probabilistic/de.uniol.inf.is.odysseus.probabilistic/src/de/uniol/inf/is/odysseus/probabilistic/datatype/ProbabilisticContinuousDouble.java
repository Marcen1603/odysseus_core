package de.uniol.inf.is.odysseus.probabilistic.datatype;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticContinuousDouble {
    private final NormalDistributionMixture distribution;
    private final int                       dimension;

    public ProbabilisticContinuousDouble(int dimension, NormalDistributionMixture distribution) {
        if (dimension < distribution.getDimension()) {
            this.dimension = dimension;
            this.distribution = distribution;
        }
        else {
            throw new RuntimeException("Invalid dimension");
        }
    }

    public ProbabilisticContinuousDouble(final ProbabilisticContinuousDouble probabilisticContinuousDouble) {
        this.dimension = probabilisticContinuousDouble.dimension;
        this.distribution = probabilisticContinuousDouble.distribution.clone();
    }

    public int getDimension() {
        return dimension;
    }

    public NormalDistributionMixture getDistribution() {
        return this.distribution;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(distribution.toString());
        sb.append(")");
        return sb.toString();
    }

    @Override
    public ProbabilisticContinuousDouble clone() {
        return new ProbabilisticContinuousDouble(this);
    }
}
