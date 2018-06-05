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

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class BinomialDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Create a binomial distribution with the given number of trials and
     * probability of success.
     * 
     * @param errorModel
     *            Error model.
     * @param trials
     *            Number of trials.
     * @param probability
     *            Probability of success.
     * @throws NotPositiveException
     *             if trials < 0.
     * @throws OutOfRangeException
     *             if probability < 0 or probability > 1.
     * 
     */
    public BinomialDistributionGenerator(IErrorModel errorModel, int trials, double probability) {
        this(errorModel, trials, probability, 1l);
    }

    /**
     * Create a binomial distribution with the given number of trials and
     * probability of success.
     * 
     * @param errorModel
     *            Error model.
     * @param trials
     *            Number of trials.
     * @param probability
     *            Probability of success.
     * @param seed
     *            Initial seed.
     * @throws NotPositiveException
     *             if trials < 0.
     * @throws OutOfRangeException
     *             if probability < 0 or probability > 1.
     * 
     */
    public BinomialDistributionGenerator(IErrorModel errorModel, int trials, double probability, long seed) {
        super(errorModel);
        this.distribution = new BinomialDistribution(trials, probability);
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
