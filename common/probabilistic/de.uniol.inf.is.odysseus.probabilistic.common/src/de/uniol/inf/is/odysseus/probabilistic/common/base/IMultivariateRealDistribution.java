/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base;

import org.apache.commons.math3.distribution.TDistribution;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IMultivariateRealDistribution {
    /**
     * For a random variable {@code X} whose values are distributed according
     * to this distribution, this method returns {@code P(a < X <= b)}. In other
     * words, this method represents the (cumulative) distribution function
     * (CDF) for this distribution.
     * 
     * @param a
     *            the point at which the CDF is evaluated
     * @param b
     *            the point at which the CDF is evaluated
     * @return the probability that a random variable with this
     *         distribution takes a value less than {@code a} and less or equal
     *         to {@code b}
     */
    double probability(final double[] a, final double[] b);

    /**
     * For a random variable {@code X} whose values are distributed according
     * to this distribution, this method returns {@code P(X <= a)}. In other
     * words, this method represents the (cumulative) distribution function
     * (CDF) for this distribution.
     * 
     * @param x
     *            the point at which the CDF is evaluated
     * @return the probability that a random variable with this
     *         distribution takes a value less than or equal to {@code x}
     */
    double probability(final double[] a);

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
    double density(double[] values);

    /**
     * Use this method to get the numerical value of the mean of this
     * distribution.
     * 
     * @return the mean or {@code Double.NaN} if it is not defined
     */
    double[] getNumericalMean();

    /**
     * Use this method to get the numerical value of the variance of this
     * distribution.
     * 
     * @return the variance (possibly {@code Double.POSITIVE_INFINITY} as
     *         for certain cases in {@link TDistribution}) or {@code Double.NaN}
     *         if it
     *         is not defined
     */
    double[][] getNumericalVariance();
}
