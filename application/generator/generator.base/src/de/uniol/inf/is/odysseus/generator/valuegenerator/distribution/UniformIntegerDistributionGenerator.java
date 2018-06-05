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
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class UniformIntegerDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Creates a new uniform integer distribution using the given lower and
     * upper bounds (both inclusive).
     * 
     * @param errorModel
     *            Error model.
     * @param lower
     *            Lower bound (inclusive) of this distribution.
     * @param upper
     *            Upper bound (inclusive) of this distribution.
     * @throws NumberIsTooLargeException
     *             if lower >= upper.
     * 
     */
    public UniformIntegerDistributionGenerator(IErrorModel errorModel, int lower, int upper) {
        this(errorModel, lower, upper, 1l);
    }

    /**
     * Creates a new uniform integer distribution using the given lower and
     * upper bounds (both inclusive).
     * 
     * @param errorModel
     *            Error model.
     * @param lower
     *            Lower bound (inclusive) of this distribution.
     * @param upper
     *            Upper bound (inclusive) of this distribution.
     * @param seed
     *            Initial seed.
     * @throws NumberIsTooLargeException
     *             if lower >= upper.
     * 
     */
    public UniformIntegerDistributionGenerator(IErrorModel errorModel, int lower, int upper, long seed) {
        super(errorModel);
        this.distribution = new UniformIntegerDistribution(lower, upper);
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
