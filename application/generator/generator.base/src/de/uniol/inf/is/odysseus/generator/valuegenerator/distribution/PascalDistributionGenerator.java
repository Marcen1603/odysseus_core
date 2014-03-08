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

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class PascalDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Create a Pascal distribution with the given number of successes and
     * probability of success.
     * 
     * @param errorModel
     *            Error model.
     * @param number
     *            Number of successes.
     * @param probability
     *            Probability of success.
     * @throws NotStrictlyPositiveException
     *             if the number of successes is not positive
     * @throws OutOfRangeException
     *             if the probability of success is not in the range [0, 1].
     * 
     */
    public PascalDistributionGenerator(IErrorModel errorModel, int number, double probability) {
        this(errorModel, number, probability, 1l);
    }

    /**
     * Create a Pascal distribution with the given number of successes and
     * probability of success.
     * 
     * @param errorModel
     *            Error model.
     * @param number
     *            Number of successes.
     * @param probability
     *            Probability of success.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if the number of successes is not positive
     * @throws OutOfRangeException
     *             if the probability of success is not in the range [0, 1].
     * 
     */
    public PascalDistributionGenerator(IErrorModel errorModel, int number, double probability, long seed) {
        super(errorModel);
        this.distribution = new PascalDistribution(number, probability);
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
