package de.uniol.inf.is.odysseus.probabilistic.datatype;

import java.io.Serializable;
import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class CovarianceMatrix implements Serializable, Cloneable {
    /**
     * 
     */
    private static final long          serialVersionUID = 1428809596935611021L;
    private final double[]             entries;
    private final NormalDistribution[] distributions;
    private final int                  size;

    public CovarianceMatrix(final double[] entries) {
        this.entries = entries;
        this.size = (int) (-0.5 + Math.sqrt(0.25 + (this.entries.length * 2)));
        this.distributions = new NormalDistribution[size];
    }

    public CovarianceMatrix(final CovarianceMatrix covarianceMatrix) {
        this.entries = Arrays.copyOf(covarianceMatrix.entries, covarianceMatrix.entries.length);
        this.distributions = Arrays.copyOf(covarianceMatrix.distributions, covarianceMatrix.distributions.length);
        this.size = covarianceMatrix.size;
    }

    /**
     * Return the entries of the triangle of the covariance matrix
     * 
     * @return The entries in the upper left triangle
     */
    public double[] getEntries() {
        return this.entries;
    }

    public RealMatrix getMatrix() {
        return CovarianceMatrixUtils.toMatrix(this);
    }

    /**
     * The connected distributions
     * 
     * @return The distributions
     */
    public NormalDistribution[] getDistributions() {
        return this.distributions;
    }

    /**
     * @param index
     * @param distribution
     */
    public void setDistribution(int index, NormalDistribution distribution) {
        this.distributions[index] = distribution;
    }

    /**
     * Return the number of colums/rows of the covariance matrix
     * 
     * @return The size of the covariance matrix
     */
    public int size() {
        return this.size;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (final double entry : this.entries) {
            if (sb.length() > 1) {
                sb.append(",");
            }
            sb.append(entry);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public CovarianceMatrix clone() {
        return new CovarianceMatrix(this);
    }

}
