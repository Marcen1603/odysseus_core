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

import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class GammaDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Creates a new gamma distribution with specified values of the shape and
     * scale parameters.
     * 
     * 
     * @param errorModel
     *            Error model.
     * @param shape
     *            the shape parameter
     * @param scale
     *            the scale parameter
     * @throws NotStrictlyPositiveException
     *             if shape <= 0 or scale <= 0.
     * 
     */
    public GammaDistributionGenerator(IErrorModel errorModel, double shape, double scale) {
        this(errorModel, shape, scale, 1l);
    }

    /**
     * Creates a new gamma distribution with specified values of the shape and
     * scale parameters.
     * 
     * 
     * @param errorModel
     *            Error model.
     * @param shape
     *            the shape parameter
     * @param scale
     *            the scale parameter
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if shape <= 0 or scale <= 0.
     */
    public GammaDistributionGenerator(IErrorModel errorModel, double shape, double scale, long seed) {
        super(errorModel);
        this.distribution = new GammaDistribution(shape, scale);
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
