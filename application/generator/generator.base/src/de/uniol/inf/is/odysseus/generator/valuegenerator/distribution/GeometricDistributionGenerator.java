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

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class GeometricDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Create a geometric distribution with the given probability of success.
     * 
     * @param errorModel
     *            Error model.
     * @param probability
     *            probability of success.
     * @throws OutOfRangeException
     *             if probability <= 0 or probability > 1.
     * 
     */
    public GeometricDistributionGenerator(IErrorModel errorModel, double probability) {
        super(errorModel);
        // this.distribution = new GeometricDistribution(probability);
        this.distribution = null;
        this.seed = 1l;
        throw new IllegalArgumentException("Not implemented yet");
    }

    /**
     * Create a geometric distribution with the given probability of success.
     * 
     * @param errorModel
     *            Error model.
     * @param probability
     *            probability of success.
     * @param seed
     *            Initial seed.
     * @throws OutOfRangeException
     *             if probability <= 0 or probability > 1.
     * 
     */
    public GeometricDistributionGenerator(IErrorModel errorModel, double probability, long seed) {
        super(errorModel);
        // this.distribution = new GeometricDistribution(probability);
        this.distribution = null;
        this.seed = seed;
        throw new IllegalArgumentException("Not implemented yet");
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
