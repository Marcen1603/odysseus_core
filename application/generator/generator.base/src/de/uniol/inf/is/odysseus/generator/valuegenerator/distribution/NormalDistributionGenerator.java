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

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class NormalDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Create a normal distribution with mean equal to zero and standard
     * deviation equal to one.
     * 
     * @param errorModel
     *            Error model.
     * 
     */
    public NormalDistributionGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);
    }

    /**
     * Create a normal distribution with mean equal to zero and standard
     * deviation equal to one.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     * 
     */
    public NormalDistributionGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        this.distribution = new NormalDistribution();
        this.seed = seed;
    }

    /**
     * Create a normal distribution using the given mean and standard deviation.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            Mean for this distribution.
     * @param standardDeviation
     *            Standard deviation for this distribution.
     * 
     * @throws NotStrictlyPositiveException
     *             if standardDeviation <= 0.
     * 
     */
    public NormalDistributionGenerator(IErrorModel errorModel, double mean, double standardDeviation) {
        this(errorModel, mean, standardDeviation, 1l);
    }

    /**
     * Create a normal distribution using the given mean and standard deviation.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            Mean for this distribution.
     * @param standardDeviation
     *            Standard deviation for this distribution.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if standardDeviation <= 0.
     * 
     */
    public NormalDistributionGenerator(IErrorModel errorModel, double mean, double standardDeviation, long seed) {
        super(errorModel);
        this.distribution = new NormalDistribution(mean, standardDeviation);
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
