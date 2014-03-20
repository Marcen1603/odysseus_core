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
package de.uniol.inf.is.odysseus.probabilistic.functions.math;

import org.apache.commons.math3.linear.RealVector;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public abstract class AbstractIntegrateFunction extends AbstractProbabilisticFunction<Double> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6128844979837446517L;

    public AbstractIntegrateFunction(final SDFDatatype[][] accTypes) {
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
    protected final double getValueInternal(final MultivariateMixtureDistribution distribution, final RealVector lowerBound, final RealVector upperBound) {
        return distribution.probability(lowerBound.toArray(), upperBound.toArray());
    }

}
