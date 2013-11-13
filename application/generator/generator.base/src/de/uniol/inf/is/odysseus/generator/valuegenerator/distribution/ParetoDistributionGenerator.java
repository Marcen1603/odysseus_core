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

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ParetoDistributionGenerator extends AbstractValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Create a Pareto distribution with a scale of 1 and a shape of 1.
     * 
     * @param errorModel
     *            Error model.
     * 
     */
    public ParetoDistributionGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);
    }

    /**
     * Create a Pareto distribution with a scale of 1 and a shape of 1.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     * 
     */
    public ParetoDistributionGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        // this.distribution = new ParetoDistribution();
        this.distribution = null;
        this.seed = seed;
        throw new IllegalArgumentException("Not implemented yet");
    }

    /**
     * Create a Pareto distribution using the specified scale and shape.
     * 
     * @param errorModel
     *            Error model.
     * @param scale
     *            - the scale parameter of this distribution
     * @param shape
     *            - the shape parameter of this distribution
     * @throws NotStrictlyPositiveException
     *             - if scale <= 0 or shape <= 0.
     * 
     */
    public ParetoDistributionGenerator(IErrorModel errorModel, double scale, double shape) {
        this(errorModel, scale, shape, 1l);
    }

    /**
     * Create a Pareto distribution using the specified scale and shape.
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
     *             if scale <= 0 or shape <= 0.
     * 
     * 
     */
    public ParetoDistributionGenerator(IErrorModel errorModel, double scale, double shape, long seed) {
        super(errorModel);
        // this.distribution = new ParetoDistribution(scale, shape);
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
