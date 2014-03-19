/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.util.Arrays;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

import de.uniol.inf.is.odysseus.probabilistic.common.CovarianceMatrixUtils;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.Matrix;
import de.uniol.inf.is.odysseus.probabilistic.math.genz.QSIMVN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MultivariateNormalDistribution implements IMultivariateDistribution {
    /**
     * 
     */
    private static final long serialVersionUID = -5482504990362339784L;
    /** &radic;(2) */
    private static final double SQRT2 = FastMath.sqrt(2.0);
    private double[] means;
    private RealMatrix covariance;
    private double covarianceDeterminant;
    private RealMatrix covarianceInverse;

    /**
     * @param means
     * @param covariances
     * @throws SingularMatrixException
     * @throws DimensionMismatchException
     * @throws NonPositiveDefiniteMatrixException
     */
    public MultivariateNormalDistribution(double[] means, double[][] covariances) throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        this.means = means;
        this.covariance = new Array2DRowRealMatrix(covariances);
    }

    public MultivariateNormalDistribution(double[] means, double[] covariancesUpper) throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        this(means, CovarianceMatrixUtils.toMatrix(covariancesUpper).getData());

    }

    /**
     * @param extendedMultivariateNormalDistribution
     */
    public MultivariateNormalDistribution(MultivariateNormalDistribution copy) {
        this.means = MathArrays.copyOf(copy.means);
        this.covariance = copy.covariance.copy();
    }

    public int getDimension() {
        return means.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double probability(double[] a) {
        if (getDimension() == 1) {
            final double dev = a[0] - means[0];
            final double standardDeviation = covariance.getEntry(0, 0);
            if (FastMath.abs(dev) > 40 * standardDeviation) {
                return dev < 0 ? 0.0d : 1.0d;
            }
            return 0.5 * (1 + Erf.erf(dev / (standardDeviation * SQRT2)));
        }
        else {
            final Matrix upper = new Matrix(new double[][] { a });
            double[] lower = new double[a.length];
            Arrays.fill(lower, Double.NEGATIVE_INFINITY);
            final Matrix covarianceMatrix = new Matrix(covariance.getData());
            return QSIMVN.cumulativeProbability(5000, covarianceMatrix, new Matrix(new double[][] { lower }), upper).getProbability();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double probability(double[] a, double[] b) {
        if (Arrays.equals(a, b)) {
            return 0.0;
        }
        if (getDimension() == 1) {
            if (a[0] > b[0]) {
                throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, a[0], b[0], true);
            }
            final double denom = covariance.getEntry(0, 0) * SQRT2;
            final double v0 = (a[0] - means[0]) / denom;
            final double v1 = (b[0] - means[0]) / denom;
            return 0.5 * Erf.erf(v0, v1);
        }
        else {
            final Matrix lower = new Matrix(new double[][] { a });
            final Matrix upper = new Matrix(new double[][] { b });
            final Matrix covarianceMatrix = new Matrix(covariance.getData());
            return QSIMVN.cumulativeProbability(5000, covarianceMatrix, lower, upper).getProbability();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getMean() {
        return MathArrays.copyOf(means);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[][] getVariance() {
        return covariance.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double density(double[] values) {
        if (covarianceInverse == null) {
            final EigenDecomposition covMatDec = new EigenDecomposition(covariance);
            covarianceInverse = covMatDec.getSolver().getInverse();
            covarianceDeterminant = covMatDec.getDeterminant();
        }

        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - means[i];
        }
        final double[] preMultiplied = covarianceInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.pow(2 * FastMath.PI, -0.5 * means.length) * FastMath.pow(covarianceDeterminant, -0.5) * FastMath.exp(-0.5 * sum);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(RealMatrix restrict) {
        final RealMatrix meansMatrix = restrict.multiply(MatrixUtils.createRealDiagonalMatrix(means)).multiply(restrict.transpose());
        this.means = new double[meansMatrix.getRowDimension()];
        for (int d = 0; d < meansMatrix.getRowDimension(); d++) {
            means[d] = meansMatrix.getEntry(d, d);
        }
        this.covariance = restrict.multiply(covariance).multiply(restrict.transpose());
        covarianceInverse = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("𝒩(");
        if (getDimension() == 1) {
            sb.append(means[0]);
            sb.append(",");
            sb.append(covariance.getEntry(0, 0));
        }
        else {
            sb.append(Arrays.toString(means));
            sb.append(",[");
            for (int i = 0; i < covariance.getRowDimension(); i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("[");
                for (int j = 0; j < covariance.getColumnDimension(); j++) {
                    if (j > 0) {
                        sb.append(", ");
                    }
                    sb.append(covariance.getEntry(i, j));
                }
                sb.append("]");
            }
            sb.append("]");
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.covariance == null) ? 0 : this.covariance.hashCode());
        result = prime * result + Arrays.hashCode(this.means);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MultivariateNormalDistribution other = (MultivariateNormalDistribution) obj;
        if (this.covariance == null) {
            if (other.covariance != null) {
                return false;
            }
        }
        else if (!this.covariance.equals(other.covariance)) {
            return false;
        }
        if (!Arrays.equals(this.means, other.means)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MultivariateNormalDistribution clone() {
        return new MultivariateNormalDistribution(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution add(Double number) {
        RealMatrix thisMean = new Array2DRowRealMatrix(means);
        RealMatrix thisCovariance = covariance;
        return new MultivariateNormalDistribution(thisMean.scalarAdd(number).getColumn(0), thisCovariance.getData());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution subtract(Double number) {
        return this.add(-number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution multiply(Double number) {
        RealMatrix thisMean = new Array2DRowRealMatrix(means);
        RealMatrix thisCovariance = covariance;
        return new MultivariateNormalDistribution(thisMean.scalarMultiply(number).getColumn(0), thisCovariance.scalarMultiply(number * number).getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution divide(Double number) {
        return this.multiply(1.0 / number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution add(IMultivariateDistribution other) {
        RealMatrix otherMean = new Array2DRowRealMatrix(other.getMean());
        RealMatrix otherCovariance = new Array2DRowRealMatrix(other.getVariance());
        RealMatrix thisMean = new Array2DRowRealMatrix(means);
        RealMatrix thisCovariance = covariance;
        return new MultivariateNormalDistribution(thisMean.add(otherMean).getColumn(0), thisCovariance.add(otherCovariance).getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution subtract(IMultivariateDistribution other) {
        RealMatrix otherMean = new Array2DRowRealMatrix(other.getMean());
        RealMatrix otherCovariance = new Array2DRowRealMatrix(other.getVariance());
        RealMatrix thisMean = new Array2DRowRealMatrix(means);
        RealMatrix thisCovariance = covariance;
        return new MultivariateNormalDistribution(thisMean.subtract(otherMean).getColumn(0), thisCovariance.add(otherCovariance).getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution multiply(IMultivariateDistribution other) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution divide(IMultivariateDistribution other) {
        // TODO Auto-generated method stub
        return null;
    }

    public static void main(String[] args) {
        double[] means = new double[] { 1.0, 2.0, 3.0 };
        double[][] covariance = new double[][] { { 1.0, 0.5, 0.5 }, { 0.5, 1.0, 0.5 }, { 0.5, 0.5, 1.0 } };

        MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariance);
        Array2DRowRealMatrix restrictMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 } });
        System.out.println("Distribution: " + distribution);
        System.out.println("Restrict to: " + restrictMatrix);
        distribution.restrict(restrictMatrix);
        System.out.println("Result: " + distribution);

        System.out.println("Add: X+3 -> " + distribution.add(3.0));
        System.out.println("Add: X-3 -> " + distribution.subtract(3.0));
        System.out.println("Add: X*3 -> " + distribution.multiply(3.0));
        System.out.println("Add: X/3 -> " + distribution.divide(3.0));

        System.out.println("Add: X+X -> " + distribution.add(distribution));
        System.out.println("Add: X-X -> " + distribution.subtract(distribution));
        System.out.println("Add: X*X -> " + distribution.multiply(distribution));
        System.out.println("Add: X/X -> " + distribution.divide(distribution));
    }
}
