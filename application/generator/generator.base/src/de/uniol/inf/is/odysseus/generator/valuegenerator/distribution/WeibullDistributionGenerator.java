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
import org.apache.commons.math3.distribution.WeibullDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class WeibullDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Create a Weibull distribution with the given shape and scale and a
     * location equal to zero.
     * 
     * @param errorModel
     *            Error model.
     * @param alpha
     *            Shape parameter.
     * @param beta
     *            Scale parameter.
     * @throws NotStrictlyPositiveException
     *             if alpha <= 0 or beta <= 0.
     * 
     */
    public WeibullDistributionGenerator(IErrorModel errorModel, double alpha, double beta) {
        this(errorModel, alpha, beta, 1l);
    }

    /**
     * Create a Weibull distribution with the given shape and scale and a
     * location equal to zero.
     * 
     * @param errorModel
     *            Error model.
     * @param alpha
     *            Shape parameter.
     * @param beta
     *            Scale parameter.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if alpha <= 0 or beta <= 0.
     * 
     */
    public WeibullDistributionGenerator(IErrorModel errorModel, double alpha, double beta, long seed) {
        super(errorModel);
        this.distribution = new WeibullDistribution(alpha, beta);
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
