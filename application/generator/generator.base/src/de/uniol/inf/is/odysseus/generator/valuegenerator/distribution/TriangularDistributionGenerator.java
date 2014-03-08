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
import org.apache.commons.math3.distribution.TriangularDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TriangularDistributionGenerator extends AbstractSingleValueGenerator {

    private final long seed;
    private final RealDistribution distribution;

    /**
     * Creates a triangular real distribution using the given lower limit, upper
     * limit, and mode.
     * 
     * @param errorModel
     *            Error model.
     * @param a
     *            Lower limit of this distribution (inclusive).
     * @param b
     *            Upper limit of this distribution (inclusive).
     * @param c
     *            Mode of this distribution.
     * @throws NumberIsTooLargeException
     *             if a >= b or if c > b.
     * @throws NumberIsTooSmallException
     *             if c < a.
     * 
     */
    public TriangularDistributionGenerator(IErrorModel errorModel, double a, double b, double c) {
        this(errorModel, a, b, c, 1l);
    }

    /**
     * Creates a triangular real distribution using the given lower limit, upper
     * limit, and mode.
     * 
     * @param errorModel
     *            Error model.
     * @param a
     *            Lower limit of this distribution (inclusive).
     * @param b
     *            Upper limit of this distribution (inclusive).
     * @param c
     *            Mode of this distribution.
     * @param seed
     *            Initial seed.
     * @throws NumberIsTooLargeException
     *             if a >= b or if c > b.
     * @throws NumberIsTooSmallException
     *             if c < a.
     * 
     */
    public TriangularDistributionGenerator(IErrorModel errorModel, double a, double b, double c, long seed) {
        super(errorModel);
        this.distribution = new TriangularDistribution(a, b, c);
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
