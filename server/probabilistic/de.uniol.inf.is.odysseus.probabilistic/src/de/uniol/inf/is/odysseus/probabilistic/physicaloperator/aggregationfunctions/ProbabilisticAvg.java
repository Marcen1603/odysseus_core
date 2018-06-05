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
public class ProbabilisticAvg extends AbstractAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6016082419401250119L;
    /** The attribute position. */
    private final int pos;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticAvg}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticAvg}
     */
    public static ProbabilisticAvg getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticAvg(pos, partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticAvg}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticAvg(final int pos, final boolean partialAggregateInput, final String datatype) {
        super("AVG", partialAggregateInput);
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
        final ProbabilisticTuple<IProbabilistic> restricted = in.restrict(this.pos, true);
        final MultivariateMixtureDistribution distribution = restricted.getDistribution(((ProbabilisticDouble) restricted.getAttribute(0)).getDistribution());
        final AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = new AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>(distribution, this.datatype);
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
        final ProbabilisticTuple<IProbabilistic> restricted = toMerge.restrict(this.pos, true);

        AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = null;
        if (createNew) {
            pa = new AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>(((AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p).getSum(), this.datatype);
        }
        else {
            pa = (AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
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
        final AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = (AvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        final ProbabilisticTuple<IProbabilistic> r = new ProbabilisticTuple<IProbabilistic>(1, 1, true);
        final MultivariateMixtureDistribution avg = pa.getAvg();
        r.setDistribution(0, avg);
        avg.setAttribute(0, 0);
        r.setAttribute(0, new ProbabilisticDouble(0));
        return r;
    }
}
