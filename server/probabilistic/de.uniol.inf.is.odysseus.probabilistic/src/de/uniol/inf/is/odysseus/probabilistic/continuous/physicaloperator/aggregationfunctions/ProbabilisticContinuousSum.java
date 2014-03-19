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
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticContinuousSum extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7877321730422944130L;
    /** The attribute position. */
    private final int pos;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticContinuousSum}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticContinuousSum}
     */
    public static ProbabilisticContinuousSum getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticContinuousSum(pos, partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticContinuousSum}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticContinuousSum(final int pos, final boolean partialAggregateInput, final String datatype) {
        super("SUM", partialAggregateInput);
        this.pos = pos;
        this.datatype = datatype;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
     * .IInitializer#init(java.lang.Object)
     */
    @Override
    public final IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
        final MultivariateMixtureDistribution distribution = in.getDistribution(((ProbabilisticContinuousDouble) in.getAttribute(this.pos)).getDistribution());
        final SumPartialAggregate<ProbabilisticTuple<?>> pa = new SumPartialAggregate<ProbabilisticTuple<?>>(distribution, this.datatype);
        return pa;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
     * .
     * IMerger#merge(de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate
     * .basefunctions.IPartialAggregate, java.lang.Object, boolean)
     */
    @Override
    public final IPartialAggregate<ProbabilisticTuple<?>> merge(final IPartialAggregate<ProbabilisticTuple<?>> p, final ProbabilisticTuple<?> toMerge, final boolean createNew) {
        SumPartialAggregate<ProbabilisticTuple<?>> pa = null;
        if (createNew) {
            pa = new SumPartialAggregate<ProbabilisticTuple<?>>(((SumPartialAggregate<ProbabilisticTuple<?>>) p).getSum(), this.datatype);
        }
        else {
            pa = (SumPartialAggregate<ProbabilisticTuple<?>>) p;
        }
        final MultivariateMixtureDistribution distribution = toMerge.getDistribution(((ProbabilisticContinuousDouble) toMerge.getAttribute(this.pos)).getDistribution());

        pa.add(distribution);
        return pa;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions
     * .
     * IEvaluator#evaluate(de.uniol.inf.is.odysseus.core.server.physicaloperator
     * .aggregate.basefunctions.IPartialAggregate)
     */
    @SuppressWarnings("rawtypes")
    @Override
    public final ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<?>> p) {
        final SumPartialAggregate<ProbabilisticTuple<?>> pa = (SumPartialAggregate<ProbabilisticTuple<?>>) p;
        final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, 1, true);
        r.setDistribution(0, pa.getSum());
        r.setAttribute(0, new ProbabilisticContinuousDouble(0));
        return r;
    }
}
