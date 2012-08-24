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
    private static final long serialVersionUID = 5858308006884394418L;
    private final byte        covarianceMatrixId;
    private final int         covarianceMatrixIndex;
    private final double      mean;

    public ProbabilisticContinuousDouble(final double mean, byte covarianceId, int covarianceIndex) {
        this.mean = mean;
        this.covarianceMatrixId = covarianceId;
        this.covarianceMatrixIndex = covarianceIndex;
    }

    public ProbabilisticContinuousDouble(final double mean, Pair<Byte, Integer> covarianceMatrix) {
        this.mean = mean;
        this.covarianceMatrixId = covarianceMatrix.getE1();
        this.covarianceMatrixIndex = covarianceMatrix.getE2();
    }

    public ProbabilisticContinuousDouble(ProbabilisticContinuousDouble probabilisticContinuousDouble) {
        this.mean = probabilisticContinuousDouble.mean;
        this.covarianceMatrixId = probabilisticContinuousDouble.covarianceMatrixId;
        this.covarianceMatrixIndex = probabilisticContinuousDouble.covarianceMatrixIndex;
    }

    public byte getCovarianceMatrixId() {
        return covarianceMatrixId;
    }

    public int getCovarianceMatrixIndex() {
        return covarianceMatrixIndex;
    }

    public double getMean() {
        return mean;
    }

    @Override
    public ProbabilisticContinuousDouble clone() {
        return new ProbabilisticContinuousDouble(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("𝒩(").append(this.mean).append(",").append(this.covarianceMatrixId).append("(")
                .append(this.covarianceMatrixIndex).append("))");
        return sb.toString();
    }

}
