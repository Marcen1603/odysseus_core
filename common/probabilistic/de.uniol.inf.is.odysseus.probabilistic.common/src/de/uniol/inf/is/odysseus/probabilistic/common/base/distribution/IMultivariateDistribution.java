/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.common.base.distribution;

import java.io.Serializable;

import org.apache.commons.math3.linear.RealMatrix;

import de.uniol.inf.is.odysseus.core.IClone;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IMultivariateDistribution extends Serializable, Cloneable, IClone {
    /**
     * For a random variable {@code X} whose values are distributed according to
     * this distribution, this method returns {@code P(a <= X <= b)}.
     * 
     * @param a
     *            the point at which the CDF is evaluated
     * @param b
     *            the point at which the CDF is evaluated
     */
    double probability(final double[] a, final double[] b);

    /**
     * For a random variable {@code X} whose values are distributed according to
     * this distribution, this method returns {@code P(X <= a)}.
     * 
     * @param x
     *            the point at which the CDF is evaluated
     */
    double probability(final double[] a);

    /**
     * Returns the probability density function (PDF) of this distribution
     * evaluated at the specified point {@code x}.
     * 
     * @param x
     *            the point at which the PDF is evaluated
     */
    double density(double[] x);

    /**
     * Gets the value of the mean of this distribution.
     * 
     */
    double[] getMean();

    /**
     * Get the value of the variance of this distribution.
     * 
     */
    double[][] getVariance();

    /**
     * Gets the value of the dimension of this distribution
     * 
     * @return
     */
    int getDimension();

    /**
     * Restrict this distribution to the dimensions given by the restrict
     * matrix.
     * 
     * @param restrict
     *            The restrict matrix
     */
    void restrict(RealMatrix restrict);

    /**
     * Gets the size of the distribution, 1 if it is not a mixture.
     * 
     * @return The size
     */
    int size();

    /**
     * Draw a sample from the distribution.
     * 
     * @return one sample vector
     */
    double[] sample();

    IMultivariateDistribution add(Double number);

    IMultivariateDistribution subtract(Double number);

    IMultivariateDistribution multiply(Double number);

    IMultivariateDistribution divide(Double number);

    IMultivariateDistribution add(IMultivariateDistribution other);

    IMultivariateDistribution subtract(IMultivariateDistribution other);

    IMultivariateDistribution multiply(IMultivariateDistribution other);

    IMultivariateDistribution divide(IMultivariateDistribution other);

    @Override
    IMultivariateDistribution clone();

}
