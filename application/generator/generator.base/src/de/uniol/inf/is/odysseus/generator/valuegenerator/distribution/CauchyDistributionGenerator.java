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

import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.RealDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class CauchyDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Creates a Cauchy distribution with the median equal to zero and scale
     * equal to one.
     * 
     * @param errorModel
     *            Error model.
     */
    public CauchyDistributionGenerator(IErrorModel errorModel) {
        this(errorModel, 1l);
    }

    /**
     * Creates a Cauchy distribution with the median equal to zero and scale
     * equal to one.
     * 
     * @param errorModel
     *            Error model.
     * @param seed
     *            Initial seed.
     */
    public CauchyDistributionGenerator(IErrorModel errorModel, long seed) {
        super(errorModel);
        this.distribution = new CauchyDistribution();
        this.seed = seed;
    }

    /**
     * Creates a Cauchy distribution using the given median and scale.
     * 
     * @param errorModel
     *            Error model.
     * @param median
     *            Median for this distribution.
     * @param scale
     *            Scale parameter for this distribution.
     * 
     */
    public CauchyDistributionGenerator(IErrorModel errorModel, double median, double scale) {
        this(errorModel, median, scale, 1l);
    }

    /**
     * Creates a Cauchy distribution using the given median and scale.
     * 
     * @param errorModel
     *            Error model.
     * @param median
     *            Median for this distribution.
     * @param scale
     *            Scale parameter for this distribution.
     * @param seed
     *            Initial seed.
     * 
     */
    public CauchyDistributionGenerator(IErrorModel errorModel, double median, double scale, long seed) {
        super(errorModel);
        this.distribution = new CauchyDistribution(median, scale);
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
