/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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
import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class RayleighDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final RealDistribution distribution;

    /**
     * 
     * Creates a new Rayleigh distribution with specified scale.
     * 
     * @param errorModel
     *            Error model.
     * @param scale
     *            the scale parameter
     */
    public RayleighDistributionGenerator(final IErrorModel errorModel, final double scale) {
        this(errorModel, scale, 1l);
    }

    /**
     * 
     * Creates a new Rayleigh distribution with specified scale.
     * 
     * @param errorModel
     *            Error model.
     * @param scale
     *            the scale parameter
     * @param seed
     *            Initial seed.
     */
    public RayleighDistributionGenerator(final IErrorModel errorModel, final double scale, final long seed) {
        super(errorModel);
        this.distribution = new RayleighDistribution(scale);
        this.seed = seed;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public double generateValue() {
        return this.distribution.sample();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void initGenerator() {
        this.distribution.reseedRandomGenerator(this.seed);
    }

    private class RayleighDistribution extends WeibullDistribution {
        /**
         * 
         */
        private static final long serialVersionUID = -1012457253383351128L;

        /**
         * @param scale
         */
        public RayleighDistribution(final double scale) {
            super(2, scale / FastMath.sqrt(2));
        }

    }

}
