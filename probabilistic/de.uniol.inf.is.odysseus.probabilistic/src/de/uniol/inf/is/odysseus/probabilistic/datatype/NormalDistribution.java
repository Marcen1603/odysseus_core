package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.collection.Pair;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NormalDistribution implements Serializable, Cloneable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5858308006884394418L;
    private final byte        covarianceMatrixId;
    private final int         covarianceMatrixIndex;
    private final double      mean;

    public NormalDistribution(final double mean, final byte covarianceId, final int covarianceIndex) {
        this.mean = mean;
        this.covarianceMatrixId = covarianceId;
        this.covarianceMatrixIndex = covarianceIndex;
    }

    public NormalDistribution(final double mean, final Pair<Byte, Integer> covarianceMatrix) {
        this.mean = mean;
        this.covarianceMatrixId = covarianceMatrix.getE1();
        this.covarianceMatrixIndex = covarianceMatrix.getE2();
    }

    public NormalDistribution(final NormalDistribution distribution) {
        this.mean = distribution.mean;
        this.covarianceMatrixId = distribution.covarianceMatrixId;
        this.covarianceMatrixIndex = distribution.covarianceMatrixIndex;
    }

    public byte getCovarianceMatrixId() {
        return this.covarianceMatrixId;
    }

    public int getCovarianceMatrixIndex() {
        return this.covarianceMatrixIndex;
    }

    public double getMean() {
        return this.mean;
    }

    @Override
    public NormalDistribution clone() {
        return new NormalDistribution(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("𝒩(").append(this.mean).append(",").append(this.covarianceMatrixId).append("(")
                .append(this.covarianceMatrixIndex).append("))");
        return sb.toString();
    }

}
