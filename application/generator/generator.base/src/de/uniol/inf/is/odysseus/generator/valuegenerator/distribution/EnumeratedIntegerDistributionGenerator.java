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

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class EnumeratedIntegerDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Create a discrete distribution using the given probability mass function
     * definition.
     * 
     * @param errorModel
     *            Error model.
     * @param singletons
     *            array of random variable values.
     * @param probabilities
     *            array of probabilities.
     * @throws DimensionMismatchException
     *             if singletons.length != probabilities.length
     * @throws NotPositiveException
     *             if any of the probabilities are negative.
     * @throws NotFiniteNumberException
     *             if any of the probabilities are infinite.
     * @throws NotANumberException
     *             if any of the probabilities are NaN.
     * @throws MathArithmeticException
     *             all of the probabilities are 0.
     * 
     */
    public EnumeratedIntegerDistributionGenerator(IErrorModel errorModel, int[] singletons, double[] probabilities) {
        this(errorModel, singletons, probabilities, 1l);
    }

    /**
     * Create a discrete distribution using the given probability mass function
     * definition.
     * 
     * @param errorModel
     *            Error model.
     * @param singletons
     *            array of random variable values.
     * @param probabilities
     *            array of probabilities.
     * @param seed
     *            Initial seed.
     * @throws DimensionMismatchException
     *             if singletons.length != probabilities.length
     * @throws NotPositiveException
     *             if any of the probabilities are negative.
     * @throws NotFiniteNumberException
     *             if any of the probabilities are infinite.
     * @throws NotANumberException
     *             if any of the probabilities are NaN.
     * @throws MathArithmeticException
     *             all of the probabilities are 0.
     * 
     */
    public EnumeratedIntegerDistributionGenerator(IErrorModel errorModel, int[] singletons, double[] probabilities, long seed) {
        super(errorModel);
        this.distribution = new EnumeratedIntegerDistribution(singletons, probabilities);
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
