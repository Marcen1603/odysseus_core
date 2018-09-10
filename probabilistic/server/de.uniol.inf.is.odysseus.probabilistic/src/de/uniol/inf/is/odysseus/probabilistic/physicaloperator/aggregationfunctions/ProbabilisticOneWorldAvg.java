/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticOneWorldAvg extends AbstractAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2188835286391575126L;
    // TODO 20140319 christian@kuka.cc Move to a global configuration
    /** The maximum error. */
    private static final double ERROR = 0.004;
    /** The probability bound. */
    private static final double BOUND = 1.0 / Math.E;
    /** The attribute position. */
    @SuppressWarnings("unused")
    private final int pos;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticOneWorldAvg}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticOneWorldAvg}
     */
    public static ProbabilisticOneWorldAvg getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticOneWorldAvg(pos, partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticOneWorldAvg}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticOneWorldAvg(final int pos, final boolean partialAggregateInput, final String datatype) {
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
        final OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = new OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>(ProbabilisticOneWorldAvg.ERROR,
                ProbabilisticOneWorldAvg.BOUND, this.datatype);
        // MultivariateMixtureDistribution distribution =
        // in.getDistribution(((ProbabilisticDouble)
        // in.getAttribute(this.pos)).getDistribution());
        // for (final Entry<Double, Double> value : distribution.entrySet()) {
        // pa.update(value.getKey(), value.getValue());
        // }
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
        OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = null;
        if (createNew) {
            pa = new OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>(ProbabilisticOneWorldAvg.ERROR, ProbabilisticOneWorldAvg.BOUND, this.datatype);
        }
        else {
            pa = (OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        }

        // for (final Entry<Double, Double> value : ((ProbabilisticDouble)
        // toMerge.getAttribute(this.pos)).getValues().entrySet()) {
        // pa.update(value.getKey(), value.getValue());
        // }
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
        final OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = (OneWorldAvgPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        final ProbabilisticTuple<IProbabilistic> r = new ProbabilisticTuple<IProbabilistic>(1, true);
        r.setAttribute(0, new Double(pa.getAggregate()));
        return r;
    }

}
