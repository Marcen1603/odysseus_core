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
package de.uniol.inf.is.odysseus.probabilistic.continuous.functions.math;

import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.ExtendedMixtureMultivariateRealDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractIntegrateMultivariateFunction extends AbstractProbabilisticFunction<Double> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6128844979837446517L;

    public AbstractIntegrateMultivariateFunction(SDFDatatype[][] accTypes) {
    	super("int", 3, accTypes, SDFDatatype.DOUBLE);
    }

    /**
     * Integrates the given distribution from the lower to the upper bound.
     * 
     * @param function
     *            The distribution
     * @param lowerBound
     *            The lower bound
     * @param upperBound
     *            The upper bound
     * @return The cumulative probability in the given bound
     */
    protected final double getValueInternal(final ProbabilisticContinuousDouble function, final RealVector lowerBound, final RealVector upperBound) {
        return this.cumulativeProbability(function, lowerBound, upperBound);
    }


    /**
     * Calculates the cumulative probability of the given distribution between
     * the lower and the upper bound.
     * 
     * @param distribution
     *            The distribution
     * @param lowerBound
     *            The lower bound
     * @param upperBound
     *            The upper bound
     * @return The cumulative probability
     */
    private double cumulativeProbability(final ProbabilisticContinuousDouble distribution, final RealVector lowerBound, final RealVector upperBound) {

        final ExtendedMixtureMultivariateRealDistribution mixtures = this.getDistributions(distribution.getDistribution());

        return mixtures.probability(lowerBound.toArray(), upperBound.toArray());
    }

}
