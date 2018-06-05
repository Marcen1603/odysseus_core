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
import org.apache.commons.math3.random.EmpiricalDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class EmpiricalDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final RealDistribution distribution;

    /**
     * Creates a new EmpiricalDistribution with the default bin count.
     * 
     * @param errorModel
     *            Error model.
     * 
     */
    public EmpiricalDistributionGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);

    }

    /**
     * Creates a new EmpiricalDistribution with the default bin count.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     * 
     */

    public EmpiricalDistributionGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        this.distribution = new EmpiricalDistribution();
        this.seed = seed;

    }

    /**
     * Creates a new EmpiricalDistribution with the specified bin count.
     * 
     * @param errorModel
     *            Error model.
     * @param binCount
     *            number of bins
     * @param seed
     *            Initial seed.
     * 
     */
    public EmpiricalDistributionGenerator(IErrorModel errorModel, int binCount) {
        this(errorModel, binCount, 1l);
    }

    /**
     * Creates a new EmpiricalDistribution with the specified bin count.
     * 
     * @param errorModel
     *            Error model.
     * @param binCount
     *            number of bins
     * @param seed
     *            Initial seed.
     * 
     */
    public EmpiricalDistributionGenerator(IErrorModel errorModel, int binCount, long seed) {
        super(errorModel);
        this.distribution = new EmpiricalDistribution(binCount);
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
