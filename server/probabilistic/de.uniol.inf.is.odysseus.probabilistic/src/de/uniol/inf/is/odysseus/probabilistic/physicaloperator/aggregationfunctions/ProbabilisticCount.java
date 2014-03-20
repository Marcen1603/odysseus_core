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
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticCount extends AbstractAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> {

    /**
     * 
     */
    private static final long serialVersionUID = -6877363889418249296L;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticCount}.
     * 
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticCount}
     */
    public static ProbabilisticCount getInstance(final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticCount(partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticAvg}.
     * 
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticCount(final boolean partialAggregateInput, final String datatype) {
        super("COUNT", partialAggregateInput);
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
        final CountPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = new CountPartialAggregate<ProbabilisticTuple<IProbabilistic>>(this.datatype);
        pa.add(in.getMetadata().getExistence());
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
        CountPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = null;
        if (createNew) {
            pa = new CountPartialAggregate<ProbabilisticTuple<IProbabilistic>>(((CountPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p).getCount(), this.datatype);
        }
        else {
            pa = (CountPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        }
        pa.add(toMerge.getMetadata().getExistence());
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
    public final ProbabilisticTuple<?> evaluate(final IPartialAggregate<ProbabilisticTuple<IProbabilistic>> p) {
        final CountPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = (CountPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        final ProbabilisticTuple<?> r = new ProbabilisticTuple(1, 0, true);
        r.setAttribute(0, pa.getCount());
        return r;
    }
}
