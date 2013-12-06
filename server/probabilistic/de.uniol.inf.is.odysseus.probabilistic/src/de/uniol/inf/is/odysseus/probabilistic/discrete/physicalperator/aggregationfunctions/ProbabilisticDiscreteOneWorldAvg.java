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
package de.uniol.inf.is.odysseus.probabilistic.discrete.physicalperator.aggregationfunctions;

import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.AbstractAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ProbabilisticDiscreteOneWorldAvg extends AbstractAggregateFunction<ProbabilisticTuple<?>, ProbabilisticTuple<?>> {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2188835286391575126L;
    // TODO Move to a global configuration
    /** The maximum error. */
    private static final double ERROR = 0.004;
    /** The probability bound. */
    private static final double BOUND = 1.0 / Math.E;
    /** The attribute position. */
    private final int pos;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticDiscreteOneWorldAvg}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticDiscreteOneWorldAvg}
     */
    public static ProbabilisticDiscreteOneWorldAvg getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticDiscreteOneWorldAvg(pos, partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticDiscreteOneWorldAvg}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticDiscreteOneWorldAvg(final int pos, final boolean partialAggregateInput, final String datatype) {
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
    public final IPartialAggregate<ProbabilisticTuple<?>> init(final ProbabilisticTuple<?> in) {
        final OneWorldAvgPartialAggregate<ProbabilisticTuple<?>> pa = new OneWorldAvgPartialAggregate<ProbabilisticTuple<?>>(ProbabilisticDiscreteOneWorldAvg.ERROR,
                ProbabilisticDiscreteOneWorldAvg.BOUND, this.datatype);
        for (final Entry<Double, Double> value : ((ProbabilisticDouble) in.getAttribute(this.pos)).getValues().entrySet()) {
            pa.update(value.getKey(), value.getValue());
        }
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
        OneWorldAvgPartialAggregate<ProbabilisticTuple<?>> pa = null;
        if (createNew) {
            pa = new OneWorldAvgPartialAggregate<ProbabilisticTuple<?>>(ProbabilisticDiscreteOneWorldAvg.ERROR, ProbabilisticDiscreteOneWorldAvg.BOUND, this.datatype);
        }
        else {
            pa = (OneWorldAvgPartialAggregate<ProbabilisticTuple<?>>) p;
        }

        for (final Entry<Double, Double> value : ((ProbabilisticDouble) toMerge.getAttribute(this.pos)).getValues().entrySet()) {
            pa.update(value.getKey(), value.getValue());
        }
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
        final OneWorldAvgPartialAggregate<ProbabilisticTuple<?>> pa = (OneWorldAvgPartialAggregate<ProbabilisticTuple<?>>) p;
        final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, true);
        r.setAttribute(0, new Double(pa.getAggregate()));
        return r;
    }

}
