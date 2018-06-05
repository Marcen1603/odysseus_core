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
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
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
    private final RandomGenerator random = new Well19937c();
    private double[] means;
    private RealMatrix covariance;
    private double covarianceDeterminant;
    private RealMatrix covarianceInverse;
    private RealMatrix samplingMatrix;

    /**
     * @param means
     * @param covariances
     * @throws SingularMatrixException
     * @throws DimensionMismatchException
     * @throws NonPositiveDefiniteMatrixException
     */
    public MultivariateNormalDistribution(final double[] means, final double[][] covariances) throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        this.means = means;
        this.covariance = new Array2DRowRealMatrix(covariances);
    }

    public MultivariateNormalDistribution(final double[] means, final double[] covariancesUpper) throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        this(means, CovarianceMatrixUtils.toMatrix(covariancesUpper).getData());

    }

    /**
     * @param extendedMultivariateNormalDistribution
     */
    public MultivariateNormalDistribution(final MultivariateNormalDistribution copy) {
        this.means = MathArrays.copyOf(copy.means);
        this.covariance = copy.covariance.copy();
    }

    @Override
    public int getDimension() {
        return this.means.length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double probability(final double[] a) {
        if (this.getDimension() == 1) {
            final double dev = a[0] - this.means[0];
            final double standardDeviation = this.covariance.getEntry(0, 0);
            if (FastMath.abs(dev) > (40 * standardDeviation)) {
                return dev < 0 ? 0.0d : 1.0d;
            }
            return 0.5 * (1 + Erf.erf(dev / (standardDeviation * MultivariateNormalDistribution.SQRT2)));
        }
        else {
            Matrix upper = new Matrix(new double[][] { a });
            upper = upper.substract(new Matrix(new double[][] { this.means }));
            final double[] lower = new double[a.length];
            Arrays.fill(lower, Double.NEGATIVE_INFINITY);
            final Matrix covarianceMatrix = new Matrix(this.covariance.getData());
            return QSIMVN.cumulativeProbability(5000, covarianceMatrix, new Matrix(new double[][] { lower }), upper).getProbability();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double probability(final double[] a, final double[] b) {
        if (Arrays.equals(a, b)) {
            return 0.0;
        }
        if (this.getDimension() == 1) {
            if (a[0] > b[0]) {
                throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, a[0], b[0], true);
            }
            final double denom = this.covariance.getEntry(0, 0) * MultivariateNormalDistribution.SQRT2;
            final double v0 = (a[0] - this.means[0]) / denom;
            final double v1 = (b[0] - this.means[0]) / denom;
            return 0.5 * Erf.erf(v0, v1);
        }
        else {
            Matrix lower = new Matrix(new double[][] { a });
            lower = lower.substract(new Matrix(new double[][] { this.means }));
            Matrix upper = new Matrix(new double[][] { b });
            upper = upper.substract(new Matrix(new double[][] { this.means }));
            final Matrix covarianceMatrix = new Matrix(this.covariance.getData());
            return QSIMVN.cumulativeProbability(5000, covarianceMatrix, lower, upper).getProbability();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getMean() {
        return MathArrays.copyOf(this.means);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[][] getVariance() {
        return this.covariance.getData();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double density(final double[] values) {
        if (this.covarianceInverse == null) {
            final EigenDecomposition covMatDec = new EigenDecomposition(this.covariance);
            this.covarianceDeterminant = covMatDec.getDeterminant();
            this.covarianceInverse = covMatDec.getSolver().getInverse();
        }

        final double[] centered = new double[values.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = values[i] - this.means[i];
        }
        final double[] preMultiplied = this.covarianceInverse.preMultiply(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.pow(2 * FastMath.PI, -0.5 * this.means.length) * FastMath.pow(this.covarianceDeterminant, -0.5) * FastMath.exp(-0.5 * sum);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restrict(final RealMatrix restrict) {
        final RealMatrix meansMatrix = restrict.multiply(MatrixUtils.createRealDiagonalMatrix(this.means)).multiply(restrict.transpose());
        this.means = new double[meansMatrix.getRowDimension()];
        for (int d = 0; d < meansMatrix.getRowDimension(); d++) {
            this.means[d] = meansMatrix.getEntry(d, d);
        }
        this.covariance = restrict.multiply(this.covariance).multiply(restrict.transpose());
        this.covarianceInverse = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("N(");
        sb.append(Arrays.toString(this.means));
        sb.append(",[");
        for (int i = 0; i < this.covariance.getRowDimension(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append("[");
            for (int j = 0; j < this.covariance.getColumnDimension(); j++) {
                if (j > 0) {
                    sb.append(", ");
                }
                sb.append(this.covariance.getEntry(i, j));
            }
            sb.append("]");
        }
        sb.append("]");
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
        result = (prime * result) + ((this.covariance == null) ? 0 : this.covariance.hashCode());
        result = (prime * result) + Arrays.hashCode(this.means);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final MultivariateNormalDistribution other = (MultivariateNormalDistribution) obj;
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

    @Override
	public double[] sample() {
        double[] point = new double[getDimension()];
        final double[] normalVals = new double[getDimension()];
        for (int j = 0; j < getDimension(); j++) {
            normalVals[j] = random.nextGaussian();
        }

        if (samplingMatrix == null) {
            final EigenDecomposition covMatDec = new EigenDecomposition(covariance);
            final double[] covMatEigenvalues = covMatDec.getRealEigenvalues();
            final Array2DRowRealMatrix covMatEigenvectors = new Array2DRowRealMatrix(getDimension(), getDimension());
            for (int v = 0; v < getDimension(); v++) {
                final double[] evec = covMatDec.getEigenvector(v).toArray();
                covMatEigenvectors.setColumn(v, evec);
            }

            final RealMatrix tmpMatrix = covMatEigenvectors.transpose();

            // Scale each eigenvector by the square root of its
            // eigenvalue.
            for (int row = 0; row < getDimension(); row++) {
                final double factor = FastMath.sqrt(covMatEigenvalues[row]);
                for (int col = 0; col < getDimension(); col++) {
                    tmpMatrix.multiplyEntry(row, col, factor);
                }
            }
            this.samplingMatrix = covMatEigenvectors.multiply(tmpMatrix);
        }
        point = samplingMatrix.operate(normalVals);
        for (int k = 0; k < getDimension(); k++) {
            point[k] += means[k];
        }
        return point;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution add(final Double number) {
        final RealMatrix thisMean = new Array2DRowRealMatrix(this.means);
        final RealMatrix thisCovariance = this.covariance;
        return new MultivariateNormalDistribution(thisMean.scalarAdd(number).getColumn(0), thisCovariance.getData());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution subtract(final Double number) {
        return this.add(-number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution multiply(final Double number) {
        final RealMatrix thisMean = new Array2DRowRealMatrix(this.means);
        final RealMatrix thisCovariance = this.covariance;
        return new MultivariateNormalDistribution(thisMean.scalarMultiply(number).getColumn(0), thisCovariance.scalarMultiply(number * number).getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution divide(final Double number) {
        return this.multiply(1.0 / number);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution add(final IMultivariateDistribution other) {
        final RealMatrix otherMean = new Array2DRowRealMatrix(other.getMean());
        final RealMatrix otherCovariance = new Array2DRowRealMatrix(other.getVariance());
        final RealMatrix thisMean = new Array2DRowRealMatrix(this.means);
        final RealMatrix thisCovariance = this.covariance;
        return new MultivariateNormalDistribution(thisMean.add(otherMean).getColumn(0), thisCovariance.add(otherCovariance).getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution subtract(final IMultivariateDistribution other) {
        final RealMatrix otherMean = new Array2DRowRealMatrix(other.getMean());
        final RealMatrix otherCovariance = new Array2DRowRealMatrix(other.getVariance());
        final RealMatrix thisMean = new Array2DRowRealMatrix(this.means);
        final RealMatrix thisCovariance = this.covariance;
        return new MultivariateNormalDistribution(thisMean.subtract(otherMean).getColumn(0), thisCovariance.add(otherCovariance).getData());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution multiply(final IMultivariateDistribution other) {
        // FIXME 20140319 christian@kuka.cc Use EM for result distribution
        // estimation
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IMultivariateDistribution divide(final IMultivariateDistribution other) {
        // FIXME 20140319 christian@kuka.cc Use EM for result distribution
        // estimation
        return null;
    }

    public static void main(final String[] args) {
        final double[] means = new double[] { 1.0, 2.0, 3.0 };
        final double[][] covariance = new double[][] { { 1.0, 0.5, 0.5 }, { 0.5, 1.0, 0.5 }, { 0.5, 0.5, 1.0 } };

        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(means, covariance);
        final Array2DRowRealMatrix restrictMatrix = new Array2DRowRealMatrix(new double[][] { { 1.0, 0.0, 0.0 }, { 0.0, 0.0, 1.0 } });
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
