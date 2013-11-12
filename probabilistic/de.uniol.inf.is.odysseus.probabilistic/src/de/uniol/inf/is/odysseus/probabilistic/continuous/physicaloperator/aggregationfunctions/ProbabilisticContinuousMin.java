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
package de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.aggregationfunctions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousMin extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -8900416639623457093L;
    /** The attribute position. */
    @SuppressWarnings("unused")
    private final int pos;
    /** The result data type. */
    @SuppressWarnings("unused")
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticContinuousMin}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticContinuousMin}
     */
    public static ProbabilisticContinuousMin getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticContinuousMin(pos, partialAggregateInput, datatype);
    }

    /**
     * Class constructor.
     * 
     * @param name
     * @param partialAggregateInput
     */
    protected ProbabilisticContinuousMin(final int pos, final boolean partialAggregateInput, final String datatype) {
        super("MIN", partialAggregateInput);
        this.pos = pos;
        this.datatype = datatype;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> init(ProbabilisticTuple<?> in) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IPartialAggregate<ProbabilisticTuple<?>> merge(IPartialAggregate<ProbabilisticTuple<?>> p, ProbabilisticTuple<?> toMerge, boolean createNew) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProbabilisticTuple<?> evaluate(IPartialAggregate<ProbabilisticTuple<?>> p) {
        // TODO Auto-generated method stub
        return null;
    }

}
