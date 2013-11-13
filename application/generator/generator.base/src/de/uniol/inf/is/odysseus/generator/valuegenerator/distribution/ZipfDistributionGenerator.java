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

import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ZipfDistributionGenerator extends AbstractValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * Create a new Zipf distribution with the given number of elements and
     * exponent.
     * 
     * @param errorModel
     *            Error model.
     * @param numberOfElements
     *            Number of elements.
     * @param exponent
     *            Exponent.
     * @throws NotStrictlyPositiveException
     *             if numberOfElements <= 0 or exponent <= 0.
     * 
     */
    public ZipfDistributionGenerator(IErrorModel errorModel, int numberOfElements, double exponent) {
        this(errorModel, numberOfElements, exponent, 1l);
    }

    /**
     * Create a new Zipf distribution with the given number of elements and
     * exponent.
     * 
     * @param errorModel
     *            Error model.
     * @param numberOfElements
     *            Number of elements.
     * @param exponent
     *            Exponent.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if numberOfElements <= 0 or exponent <= 0.
     * 
     */
    public ZipfDistributionGenerator(IErrorModel errorModel, int numberOfElements, double exponent, long seed) {
        super(errorModel);
        this.distribution = new ZipfDistribution(numberOfElements, exponent);
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
