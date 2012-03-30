/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/**
 * Implementation of the uniform real distribution.
 *
 * @see <a href="http://en.wikipedia.org/wiki/Uniform_distribution_(continuous)"
 * >Uniform distribution (continuous), at Wikipedia</a>
 *
 * @version $Id: UniformRealDistribution.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 3.0
 */
public class UniformRealDistribution extends AbstractRealDistribution {
    /** Default inverse cumulative probability accuracy. */
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    /** Serializable version identifier. */
    private static final long serialVersionUID = 20120109L;

    /** Lower bound of this distribution (inclusive). */
    private final double lower;

    /** Upper bound of this distribution (exclusive). */
    private final double upper;

    /** Inverse cumulative probability accuracy. */
    private final double solverAbsoluteAccuracy;

    /**
     * Create a uniform real distribution using the given lower and upper
     * bounds.
     *
     * @param lower Lower bound of this distribution (inclusive).
     * @param upper Upper bound of this distribution (exclusive).
     * @throws NumberIsTooLargeException if {@code lower >= upper}.
     */
    public UniformRealDistribution(double lower, double upper)
        throws NumberIsTooLargeException {
        this(lower, upper, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    /**
     * Create a normal distribution using the given mean, standard deviation and
     * inverse cumulative distribution accuracy.
     *
     * @param lower Lower bound of this distribution (inclusive).
     * @param upper Upper bound of this distribution (exclusive).
     * @param inverseCumAccuracy Inverse cumulative probability accuracy.
     * @throws NumberIsTooLargeException if {@code lower >= upper}.
     */
    public UniformRealDistribution(double lower, double upper, double inverseCumAccuracy)
        throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(
                            LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND,
                            lower, upper, false);
        }

        this.lower = lower;
        this.upper = upper;
        solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    /**
     * Create a standard uniform real distribution with lower bound (inclusive)
     * equal to zero and upper bound (exclusive) equal to one.
     */
    public UniformRealDistribution() {
        this(0, 1);
    }

    /**
     * {@inheritDoc}
     *
     * For this distribution {@code P(X = x)} always evaluates to 0.
     *
     * @return 0
     */
    @Override
	public double probability(double x) {
        return 0.0;
    }

    /** {@inheritDoc} */
    @Override
	public double density(double x) {
        if (x < lower || x > upper) {
            return 0.0;
        }
        return 1 / (upper - lower);
    }

    /** {@inheritDoc} */
    @Override
	public double cumulativeProbability(double x)  {
        if (x <= lower) {
            return 0;
        }
        if (x >= upper) {
            return 1;
        }
        return (x - lower) / (upper - lower);
    }

    /** {@inheritDoc} */
    @Override
    protected double getSolverAbsoluteAccuracy() {
        return solverAbsoluteAccuracy;
    }

    /**
     * {@inheritDoc}
     *
     * For lower bound {@code lower} and upper bound {@code upper}, the mean is
     * {@code 0.5 * (lower + upper)}.
     */
    @Override
	public double getNumericalMean() {
        return 0.5 * (lower + upper);
    }

    /**
     * {@inheritDoc}
     *
     * For lower bound {@code lower} and upper bound {@code upper}, the
     * variance is {@code (upper - lower)^2 / 12}.
     */
    @Override
	public double getNumericalVariance() {
        double ul = upper - lower;
        return ul * ul / 12;
    }

    /**
     * {@inheritDoc}
     *
     * The lower bound of the support is equal to the lower bound parameter
     * of the distribution.
     *
     * @return lower bound of the support
     */
    @Override
	public double getSupportLowerBound() {
        return lower;
    }

    /**
     * {@inheritDoc}
     *
     * The upper bound of the support is equal to the upper bound parameter
     * of the distribution.
     *
     * @return upper bound of the support
     */
    @Override
	public double getSupportUpperBound() {
        return upper;
    }

    /** {@inheritDoc} */
    @Override
	public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
	public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * The support of this distribution is connected.
     *
     * @return {@code true}
     */
    @Override
	public boolean isSupportConnected() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public double sample()  {
        return randomData.nextUniform(lower, upper, true);
    }
}
