/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.generator.valuegenerator.distribution;

import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class UniformRealDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final RealDistribution distribution;

    /**
     * Create a standard uniform real distribution with lower bound (inclusive)
     * equal to zero and upper bound (exclusive) equal to one.
     * 
     * @param errorModel
     *            Error model.
     */
    public UniformRealDistributionGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);
    }

    /**
     * Create a standard uniform real distribution with lower bound (inclusive)
     * equal to zero and upper bound (exclusive) equal to one.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     */
    public UniformRealDistributionGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        this.distribution = new UniformRealDistribution();
        this.seed = seed;
    }

    /**
     * Create a uniform real distribution using the given lower and upper
     * bounds.
     * 
     * @param errorModel
     *            Error model.
     * @param lower
     *            Lower bound of this distribution (inclusive).
     * @param upper
     *            Upper bound of this distribution (exclusive).
     * @throws NumberIsTooLargeException
     *             if lower >= upper.
     * 
     */
    public UniformRealDistributionGenerator(IErrorModel errorModel, double lower, double upper) {
        this(errorModel, lower, upper, 1l);
    }

    /**
     * Create a uniform real distribution using the given lower and upper
     * bounds.
     * 
     * @param errorModel
     *            Error model.
     * @param lower
     *            Lower bound of this distribution (inclusive).
     * @param upper
     *            Upper bound of this distribution (exclusive).
     * @param seed
     *            Initial seed.
     * @throws NumberIsTooLargeException
     *             if lower >= upper.
     * 
     */
    public UniformRealDistributionGenerator(IErrorModel errorModel, double lower, double upper, long seed) {
        super(errorModel);
        this.distribution = new UniformRealDistribution(lower, upper);
        this.seed = seed;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public double generateValue() {
        return distribution.sample();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void initGenerator() {
        this.distribution.reseedRandomGenerator(this.seed);
    }
}
