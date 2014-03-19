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

import de.uniol.inf.is.odysseus.core.mep.IExpression;
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
    private List<MultivariateMixtureDistribution> distributions = new ArrayList<MultivariateMixtureDistribution>();
    private List<Integer> positions = new ArrayList<Integer>();

    public AbstractProbabilisticFunction(final String symbol, final int arity, final SDFDatatype[][] accTypes, final SDFDatatype returnType) {
        super(symbol, arity, accTypes, returnType);
    }

    public AbstractProbabilisticFunction(final String symbol, final int arity, final SDFDatatype[] accTypes, final SDFDatatype returnType) {
        super(symbol, arity, accTypes, returnType);
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
        return this.distributions;
    }

    public final void setDistributions(final MultivariateMixtureDistribution... distributions) {
        this.distributions.clear();
        for (int i = 0; i < distributions.length; i++) {
            this.setDistribution(i, distributions[i]);
        }
    }

    public void setDistribution(final int index, final MultivariateMixtureDistribution distribution) {
        this.distributions.set(index, distribution);
    }

    public final void setPositions(final Integer... positions) {
        if (positions.length != this.getArity()) {
            throw new IllegalArgumentException("illegal number of position mappings for function " + this.getSymbol());
        }

        for (int i = 0; i < positions.length; i++) {
            this.setPosition(i, positions[i]);
        }
    }

    public final List<Integer> getPositions() {
        return this.positions;
    }

    public final int getPosition(final int index) {
        return this.positions.get(index);
    }

    public void setPosition(final int index, final int position) {
        this.positions.set(index, position);
    }

    /**
     * @param positions
     * 
     */
    public void propagatePositionReference(final List<Integer> positions) {
        this.positions = positions;
        final IExpression<?>[] arguments = this.getArguments();
        for (final IExpression<?> arg : arguments) {
            if (arg instanceof AbstractProbabilisticFunction) {
                ((AbstractProbabilisticFunction<?>) arg).propagatePositionReference(positions);
            }
        }
    }

    public void propagateDistributionReference(final List<MultivariateMixtureDistribution> distributions) {
        this.distributions = distributions;
        final IExpression<?>[] arguments = this.getArguments();
        for (final IExpression<?> arg : arguments) {
            if (arg instanceof AbstractProbabilisticFunction) {
                ((AbstractProbabilisticFunction<?>) arg).propagateDistributionReference(distributions);
            }
        }
    }
}
