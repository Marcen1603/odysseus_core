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

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class HypergeometricDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Construct a new hypergeometric distribution with the specified population
     * size, number of successes in the population, and sample size.
     * 
     * @param errorModel
     *            Error model.
     * @param populationSize
     *            Population size.
     * @param numberOfSuccesses
     *            Number of successes in the population.
     * @param sampleSize
     *            Sample size.
     * @throws NotPositiveException
     *             if numberOfSuccesses < 0.
     * @throws NotStrictlyPositiveException
     *             if populationSize <= 0.
     * @throws NumberIsTooLargeException
     *             if numberOfSuccesses > populationSize, or sampleSize >
     *             populationSize.
     * 
     */
    public HypergeometricDistributionGenerator(IErrorModel errorModel, int populationSize, int numberOfSuccesses, int sampleSize) {
        this(errorModel, populationSize, numberOfSuccesses, sampleSize, 1l);
    }

    /**
     * Construct a new hypergeometric distribution with the specified population
     * size, number of successes in the population, and sample size.
     * 
     * @param errorModel
     *            Error model.
     * @param populationSize
     *            Population size.
     * @param numberOfSuccesses
     *            Number of successes in the population.
     * @param sampleSize
     *            Sample size.
     * @param seed
     *            Initial seed.
     * @throws NotPositiveException
     *             if numberOfSuccesses < 0.
     * @throws NotStrictlyPositiveException
     *             if populationSize <= 0.
     * @throws NumberIsTooLargeException
     *             if numberOfSuccesses > populationSize, or sampleSize >
     *             populationSize.
     * 
     */
    public HypergeometricDistributionGenerator(IErrorModel errorModel, int populationSize, int numberOfSuccesses, int sampleSize, long seed) {
        super(errorModel);
        this.distribution = new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize);
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
