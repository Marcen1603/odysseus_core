/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base;

import java.util.Arrays;

import org.apache.commons.math3.distribution.MultivariateRealDistribution;
import org.apache.commons.math3.linear.RealMatrix;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.probabilistic.common.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TupleDistribution {
    /** The attribute positions. */
    private int[] attributes;
    /** The scale. */
    private double scale;
    /** The support for each dimension. */
    private Interval[] support;
    /** The distribution. */
    private ExtendedMixtureMultivariateRealDistribution<? extends MultivariateRealDistribution> distribution;

    /**
     * Gets the dimension of this distribution mixture.
     * 
     * @return The dimension
     */
    public final int getDimension() {
        return this.attributes.length;
    }

    /**
     * Gets the value of the scale property.
     * 
     * @return the scale
     */
    public final double getScale() {
        return this.scale;
    }

    /**
     * Gets the value of the support property in the given dimension.
     * 
     * @param dimension
     *            the dimension
     * @return the support
     */
    public final Interval getSupport(final int dimension) {
        Preconditions.checkPositionIndex(dimension, this.support.length);
        return this.support[dimension];
    }

    /**
     * Gets the value of the support property.
     * 
     * @return the support
     */
    public final Interval[] getSupport() {
        return this.support;
    }

    /**
     * Gets the value of the attributes property in the given dimension.
     * 
     * @param dimension
     *            the dimension
     * @return the attributes
     */
    public final int getAttribute(final int dimension) {
        Preconditions.checkPositionIndex(dimension, this.attributes.length);
        return this.attributes[dimension];
    }

    /**
     * Gets the value of the attributes property.
     * 
     * @return the attributes
     */
    public final int[] getAttributes() {
        return this.attributes;
    }

    public TupleDistribution restrict(RealMatrix restrictMatrix) {
        return null;
    }

    /**
     * For a random variable {@code X} whose values are distributed according
     * to this distribution, this method returns {@code P(X = x)}. In other
     * words, this method represents the probability mass function (PMF)
     * for the distribution.
     * 
     * @param x
     *            the point at which the PMF is evaluated
     * @return the value of the probability mass function at point {@code x}
     */
    public  double probability(double[] x){
    //   return distribution.probability(x);
        return 0d;
    }

    /**
     * Returns the probability density function (PDF) of this distribution
     * evaluated at the specified point {@code x}. In general, the PDF is
     * the derivative of the {@link #cumulativeProbability(double) CDF}.
     * If the derivative does not exist at {@code x}, then an appropriate
     * replacement should be returned, e.g. {@code Double.POSITIVE_INFINITY},
     * {@code Double.NaN}, or the limit inferior or limit superior of the
     * difference quotient.
     * 
     * @param x
     *            the point at which the PDF is evaluated
     * @return the value of the probability density function at point {@code x}
     */
    public double density(double[] x) {
        return distribution.density(x);
    }

    /**
     * For a random variable {@code X} whose values are distributed according
     * to this distribution, this method returns {@code P(X <= x)}. In other
     * words, this method represents the (cumulative) distribution function
     * (CDF) for this distribution.
     * 
     * @param x
     *            the point at which the CDF is evaluated
     * @return the probability that a random variable with this
     *         distribution takes a value less than or equal to {@code x}
     */
    public double cumulativeProbability(double[] x) {
//        return distribution.cumulativeProbability(x);
        return 0d;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.attributes);
        result = prime * result + ((this.distribution == null) ? 0 : this.distribution.hashCode());
        long temp;
        temp = Double.doubleToLongBits(this.scale);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + Arrays.hashCode(this.support);
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
        TupleDistribution other = (TupleDistribution) obj;
        if (!Arrays.equals(this.attributes, other.attributes)) {
            return false;
        }
        if (Double.doubleToLongBits(this.scale) != Double.doubleToLongBits(other.scale)) {
            return false;
        }
        if (!Arrays.equals(this.support, other.support)) {
            return false;
        }
        if (this.distribution == null) {
            if (other.distribution != null) {
                return false;
            }
        }
        else if (!this.distribution.equals(other.distribution)) {
            return false;
        }
        return true;
    }

}
