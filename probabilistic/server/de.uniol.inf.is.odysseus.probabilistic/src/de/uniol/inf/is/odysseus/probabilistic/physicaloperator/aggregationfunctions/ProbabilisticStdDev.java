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
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 *         FIXME 20140319 christian@kuka.cc Implement probabilistic StdDev
 *         aggregation function
 */
public class ProbabilisticStdDev extends AbstractAggregateFunction<ProbabilisticTuple<IProbabilistic>, ProbabilisticTuple<?>> {
    /**
	 * 
	 */
    private static final long serialVersionUID = -45894921488698597L;
    /** The attribute position. */
    @SuppressWarnings("unused")
    private final int pos;
    /** The result data type. */
    private final String datatype;

    /**
     * Gets an instance of {@link ProbabilisticStdDev}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     * @return An instance of {@link ProbabilisticStdDev}
     */
    public static ProbabilisticStdDev getInstance(final int pos, final boolean partialAggregateInput, final String datatype) {
        return new ProbabilisticStdDev(pos, partialAggregateInput, datatype);
    }

    /**
     * Creates a new instance of {@link ProbabilisticStdDev}.
     * 
     * @param pos
     *            The attribute position
     * @param partialAggregateInput
     *            The partial aggregate input
     * @param datatype
     *            The result datatype
     */
    protected ProbabilisticStdDev(final int pos, final boolean partialAggregateInput, final String datatype) {
        super("STDDEV", partialAggregateInput);
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
        return new ElementPartialAggregate<ProbabilisticTuple<IProbabilistic>>(in, this.datatype);
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
        final ElementPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = null;

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
        final ElementPartialAggregate<ProbabilisticTuple<IProbabilistic>> pa = (ElementPartialAggregate<ProbabilisticTuple<IProbabilistic>>) p;
        return pa.getElem();
    }
}
