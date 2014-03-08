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

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class LogNormalDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Create a log-normal distribution, where the mean and standard deviation
     * of the normally distributed natural logarithm of the log-normal
     * distribution are equal to zero and one respectively. In other words, the
     * scale of the returned distribution is 0, while its shape is 1.
     * 
     * @param errorModel
     *            Error model.
     * 
     */
    public LogNormalDistributionGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);
    }

    /**
     * Create a log-normal distribution, where the mean and standard deviation
     * of the normally distributed natural logarithm of the log-normal
     * distribution are equal to zero and one respectively. In other words, the
     * scale of the returned distribution is 0, while its shape is 1.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     * 
     */
    public LogNormalDistributionGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        this.distribution = new LogNormalDistribution();
        this.seed = seed;
    }

    /**
     * Create a log-normal distribution using the specified scale and shape.
     * 
     * @param errorModel
     *            Error model.
     * @param scale
     *            the scale parameter of this distribution
     * @param shape
     *            the shape parameter of this distribution
     * 
     * @throws NotStrictlyPositiveException
     *             if shape <= 0.
     * 
     */
    public LogNormalDistributionGenerator(IErrorModel errorModel, double scale, double shape) {
        this(errorModel, scale, shape, 1l);
    }

    /**
     * Create a log-normal distribution using the specified scale and shape.
     * 
     * @param errorModel
     *            Error model.
     * @param scale
     *            the scale parameter of this distribution
     * @param shape
     *            the shape parameter of this distribution
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if shape <= 0.
     * 
     */
    public LogNormalDistributionGenerator(IErrorModel errorModel, double scale, double shape, long seed) {
        super(errorModel);
        this.distribution = new LogNormalDistribution(scale, shape);
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
