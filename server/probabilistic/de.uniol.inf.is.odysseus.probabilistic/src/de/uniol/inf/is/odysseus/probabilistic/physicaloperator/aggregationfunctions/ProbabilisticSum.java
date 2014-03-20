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
package de.uniol.inf.is.odysseus.probabilistic.physicaloperator.aggregationfunctions;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticSum extends AbstractAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7877321730422944130L;
    /** The attribute position. */
    private final int pos;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticSum}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticSum}
     */
    public static ProbabilisticSum getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticSum(pos, partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticSum}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticSum(final int pos, final boolean partialAggregateInput, final String datatype) {
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
    public final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> init(final ProbabilisticTuple<IProbabilistic> in) {
        ProbabilisticTuple<IProbabilistic> restricted = in.restrict(pos, true);
        final MultivariateMixtureDistribution distribution = restricted.getDistribution(((ProbabilisticDouble) restricted.getAttribute(0)).getDistribution());
        final SumPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = new SumPartialAggregate<ProbabilisticTuple<IProbabilistic>>(distribution, this.datatype);
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
    public final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> merge(final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> p, final ProbabilisticTuple<IProbabilistic> toMerge,
            final boolean createNew) {
        ProbabilisticTuple<IProbabilistic> restricted = toMerge.restrict(pos, true);
        SumPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = null;
        if (createNew) {
            pa = new SumPartialAggregate<ProbabilisticTuple<IProbabilistic>>(((SumPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p).getSum(), this.datatype);
        }
        else {
            pa = (SumPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        }
        final MultivariateMixtureDistribution distribution = restricted.getDistribution(((ProbabilisticDouble) restricted.getAttribute(0)).getDistribution());

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
    @Override
    public final ProbabilisticTuple<IProbabilistic> evaluate(final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> p) {
        final SumPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = (SumPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        final ProbabilisticTuple<IProbabilistic> r = new ProbabilisticTuple<IProbabilistic>(1, 1, true);
        final MultivariateMixtureDistribution sum = pa.getSum();
        r.setDistribution(0, sum);
        sum.setAttribute(0, 0);
        r.setAttribute(0, new ProbabilisticDouble(0));
        return r;
    }
}
