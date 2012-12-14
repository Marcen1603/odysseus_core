package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;

import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class NormalDistribution implements Serializable, Cloneable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 5858308006884394418L;
    /** The unique id of the covariance matrix for this distribution */
    private CovarianceMatrix  covarianceMatrix;
    /** The mean of the distribution */
    private final double[]    mean;

    public NormalDistribution(final double mean, final CovarianceMatrix covarianceMatrix) {
        this(new double[] { mean }, covarianceMatrix);
    }

    public NormalDistribution(final double[] mean, final CovarianceMatrix covarianceMatrix) {
        this.mean = mean;

        this.covarianceMatrix = covarianceMatrix;
    }

    public NormalDistribution(final NormalDistribution distribution) {
        this.mean = distribution.mean;
        this.covarianceMatrix = distribution.covarianceMatrix.clone();
    }

    public CovarianceMatrix getCovarianceMatrix() {
        return this.covarianceMatrix;
    }

    public double[] getMean() {
        return this.mean;
    }

    public double getMean(int dimension) {
        return this.mean[dimension];
    }

    @Override
    public NormalDistribution clone() {
        return new NormalDistribution(this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("𝒩({");
        for (int i = 0; i < mean.length; i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(this.mean[i]);
        }
        sb.append("},").append(this.covarianceMatrix).append(")");
        return sb.toString();
    }

    public void setCovarianceMatrix(CovarianceMatrix matrix) {
        this.covarianceMatrix = matrix;
    }

    public void setCovarianceMatrix(RealMatrix matrix) {
        // TODO Auto-generated method stub

    }
}
