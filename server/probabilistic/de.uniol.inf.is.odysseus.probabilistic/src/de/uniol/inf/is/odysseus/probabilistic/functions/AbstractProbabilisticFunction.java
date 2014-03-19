/*
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

package de.uniol.inf.is.odysseus.probabilistic.functions;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public abstract class AbstractProbabilisticFunction<T> extends AbstractFunction<T> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1726038091049996390L;
    /** The distributions. */
    private final List<MultivariateMixtureDistribution> distributions = new ArrayList<MultivariateMixtureDistribution>();

    public AbstractProbabilisticFunction(String symbol, int arity, SDFDatatype[][] accTypes, SDFDatatype returnType) {
    	super(symbol,arity,accTypes,returnType);
	}
    
    public AbstractProbabilisticFunction(String symbol, int arity, SDFDatatype[] accTypes, SDFDatatype returnType) {
    	super(symbol,arity,accTypes,returnType);
	}
    
    /**
     * Gets the normal distribution mixtures at the given position.
     * 
     * @param pos
     *            The position of the mixtures
     * @return The normal distribution mixtures at the given position
     */
    public final MultivariateMixtureDistribution getDistributions(final int pos) {
        Preconditions.checkPositionIndex(pos, this.distributions.size());
        return this.distributions.get(pos);
    }

    /**
     * Gets all normal distribution mixtures.
     * 
     * @return All normal distribution mixtures
     */
    public final List<MultivariateMixtureDistribution> getDistributions() {
        // return Collections.unmodifiableList(this.distributions);
        return this.distributions;

    }
}
