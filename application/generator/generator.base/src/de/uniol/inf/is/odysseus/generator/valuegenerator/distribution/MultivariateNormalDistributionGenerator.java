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

import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateRealDistribution;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractMultiValueGenerator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MultivariateNormalDistributionGenerator extends AbstractMultiValueGenerator {

    private final long seed;
    private final MultivariateRealDistribution distribution;

    /**
     * Create a multivariate normal distribution with means equal to zero and
     * covariances equal to one.
     * 
     * @param errorModel
     *            Error model.
     * 
     */
    public MultivariateNormalDistributionGenerator(IErrorModel errorModel, int dimension) {
        super(errorModel);
        double[] means = new double[dimension];
        double[][] covariances = new double[dimension][dimension];
        for (int i = 0; i < covariances.length; i++) {
            covariances[i][i] = 1.0;
        }
        this.distribution = new MultivariateNormalDistribution(means, covariances);
        this.seed = 1l;
    }

    /**
     * Create a multivariate normal distribution using the given mean and
     * standard deviation.
     * 
     * @param errorModel
     *            Error model.
     * @param means
     *            Means for this distribution.
     * @param covariances
     *            Covariances for this distribution.
     * 
     * @throws NotStrictlyPositiveException
     *             if standardDeviation <= 0.
     * 
     */
    public MultivariateNormalDistributionGenerator(IErrorModel errorModel, Double[] means, Double[][] covariances) {
        this(errorModel, means, covariances, 1l);
    }

    /**
     * Create a multivariate normal distribution using the given mean and
     * standard deviation.
     * 
     * @param errorModel
     *            Error model.
     * @param means
     *            Means for this distribution.
     * @param covariances
     *            Covariances for this distribution.
     * @param seed
     *            Initial seed.
     * @throws NotStrictlyPositiveException
     *             if standardDeviation <= 0.
     * 
     */
    public MultivariateNormalDistributionGenerator(IErrorModel errorModel, Double[] means, Double[][] covariances, long seed) {
        super(errorModel);
        Objects.requireNonNull(means);
        Objects.requireNonNull(covariances);
        double[][] covariancesPrimitives = new double[covariances.length][];
        for (int i = 0; i < covariances.length; i++) {
            covariancesPrimitives[i] = ArrayUtils.toPrimitive(covariances[i]);
        }
        this.distribution = new MultivariateNormalDistribution(ArrayUtils.toPrimitive(means), covariancesPrimitives);
        this.seed = seed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int dimension() {
        return this.distribution.getDimension();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public double[] generateValue() {
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
