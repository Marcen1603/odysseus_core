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
import org.apache.commons.math3.distribution.PoissonDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class PoissonDistributionGenerator extends AbstractSingleValueGenerator {
    private final long seed;
    private final IntegerDistribution distribution;

    /**
     * 
     * Creates a new Poisson distribution with specified mean.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            the Poisson mean
     * @throws NotStrictlyPositiveException
     *             if mean <= 0.
     */
    public PoissonDistributionGenerator(IErrorModel errorModel, double mean) {
        this(errorModel, mean, 1l);
    }

    /**
     * 
     * Creates a new Poisson distribution with specified mean.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            the Poisson mean
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if mean <= 0.
     */
    public PoissonDistributionGenerator(IErrorModel errorModel, double mean, long seed) {
        super(errorModel);
        this.distribution = new PoissonDistribution(mean);
        this.seed = seed;
    }

    /**
     * 
     * Creates a new Poisson distribution with the specified mean and
     * convergence criterion.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            the Poisson mean
     * @param epsilon
     *            Convergence criterion for cumulative probabilities.
     * @throws NotStrictlyPositiveException
     *             if mean <= 0.
     */
    public PoissonDistributionGenerator(IErrorModel errorModel, double mean, double epsilon) {
        this(errorModel, mean, epsilon, 1l);
    }

    /**
     * 
     * Creates a new Poisson distribution with the specified mean and
     * convergence criterion.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            the Poisson mean
     * @param epsilon
     *            Convergence criterion for cumulative probabilities.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if mean <= 0.
     */
    public PoissonDistributionGenerator(IErrorModel errorModel, double mean, double epsilon, long seed) {
        super(errorModel);
        this.distribution = new PoissonDistribution(mean, epsilon);
        this.seed = seed;
    }

    /**
     * 
     * Creates a new Poisson distribution with specified mean, convergence
     * criterion and maximum number of iterations.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            the Poisson mean
     * @param epsilon
     *            Convergence criterion for cumulative probabilities.
     * @param maxIterations
     *            the maximum number of iterations for cumulative probabilities.
     * @throws NotStrictlyPositiveException
     *             if mean <= 0.
     */
    public PoissonDistributionGenerator(IErrorModel errorModel, double mean, double epsilon, int maxIterations) {
        this(errorModel, mean, epsilon, maxIterations, 1l);
    }

    /**
     * 
     * Creates a new Poisson distribution with specified mean, convergence
     * criterion and maximum number of iterations.
     * 
     * @param errorModel
     *            Error model.
     * @param mean
     *            the Poisson mean
     * @param epsilon
     *            Convergence criterion for cumulative probabilities.
     * @param maxIterations
     *            the maximum number of iterations for cumulative probabilities.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if mean <= 0.
     */
    public PoissonDistributionGenerator(IErrorModel errorModel, double mean, double epsilon, int maxIterations, long seed) {
        super(errorModel);
        this.distribution = new PoissonDistribution(mean, epsilon, maxIterations);
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
