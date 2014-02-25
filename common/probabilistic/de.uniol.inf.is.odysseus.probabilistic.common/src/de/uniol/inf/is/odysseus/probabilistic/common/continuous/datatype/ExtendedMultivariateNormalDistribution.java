/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.special.Erf;
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.probabilistic.common.base.IMultivariateRealDistribution;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ExtendedMultivariateNormalDistribution extends MultivariateNormalDistribution implements IMultivariateRealDistribution {
    /** &radic;(2) */
    private static final double SQRT2 = FastMath.sqrt(2.0);

    /**
     * @param means
     * @param covariances
     * @throws SingularMatrixException
     * @throws DimensionMismatchException
     * @throws NonPositiveDefiniteMatrixException
     */
    public ExtendedMultivariateNormalDistribution(double[] means, double[][] covariances) throws SingularMatrixException, DimensionMismatchException, NonPositiveDefiniteMatrixException {
        super(means, covariances);
    }

    /**
     * @param rng
     * @param means
     * @param covariances
     * @throws SingularMatrixException
     * @throws DimensionMismatchException
     * @throws NonPositiveDefiniteMatrixException
     */
    public ExtendedMultivariateNormalDistribution(RandomGenerator rng, double[] means, double[][] covariances) throws SingularMatrixException, DimensionMismatchException,
            NonPositiveDefiniteMatrixException {
        super(rng, means, covariances);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double probability(double[] a) {
        if (getDimension() == 1) {
            final double dev = a[0] - getMeans()[0];
            final double standardDeviation = getCovariances().getData()[0][0];
            if (FastMath.abs(dev) > 40 * standardDeviation) {
                return dev < 0 ? 0.0d : 1.0d;
            }
            return 0.5 * (1 + Erf.erf(dev / (standardDeviation * SQRT2)));
        }
        else {
            // TODO Call Genz
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double probability(double[] a, double[] b) {
        if (getDimension() == 1) {
            if (a[0] > b[0]) {
                throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT, a[0], b[0], true);
            }
            final double denom = getCovariances().getData()[0][0] * SQRT2;
            final double v0 = (a[0] - getMeans()[0]) / denom;
            final double v1 = (b[0] - getMeans()[0]) / denom;
            return 0.5 * Erf.erf(v0, v1);
        }
        else {
            // TODO Call Genz
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getNumericalMean() {
        return getMeans();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[][] getNumericalVariance() {
        return getCovariances().getData();
    }

}
