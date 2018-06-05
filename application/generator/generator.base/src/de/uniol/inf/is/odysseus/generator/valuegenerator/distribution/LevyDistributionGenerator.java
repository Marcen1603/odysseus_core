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

import org.apache.commons.math3.distribution.LevyDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class LevyDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Creates a LevyDistribution.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            location
     * @param scale
     *            scale parameter
     * 
     */
    public LevyDistributionGenerator(IErrorModel errorModel, double mean, double scale) {
        this(errorModel, new Well19937c(), mean, scale, 1l);
    }

    /**
     * Creates a LevyDistribution.
     * 
     * @param errorModel
     *            Error model.
     * @param rng
     *            random generator to be used for sampling
     * @param mean
     *            location
     * @param scale
     *            scale parameter
     * 
     */
    public LevyDistributionGenerator(IErrorModel errorModel, RandomGenerator rng, double mean, double scale) {
        this(errorModel, rng, mean, scale, 1l);
    }

    /**
     * Creates a LevyDistribution.
     * 
     * @param errorModel
     *            Error model.
     * @param rng
     *            random generator to be used for sampling
     * @param mean
     *            location
     * @param scale
     *            scale parameter
     * @param seed
     *            Initial seed.
     * 
     */
    public LevyDistributionGenerator(IErrorModel errorModel, RandomGenerator rng, double mean, double scale, long seed) {
        super(errorModel);
        this.distribution = new LevyDistribution(rng, mean, scale);
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
