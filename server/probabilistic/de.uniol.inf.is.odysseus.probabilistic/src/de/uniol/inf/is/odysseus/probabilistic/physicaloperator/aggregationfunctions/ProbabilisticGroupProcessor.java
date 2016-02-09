/********************************************************************************** 
 * Copyright 2014 The Odysseus Team
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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticGroupProcessor<T extends IMetaAttribute> extends RelationalGroupProcessor<T>  {
    public ProbabilisticGroupProcessor(final SDFSchema inputSchema, final SDFSchema outputSchema, final List<SDFAttribute> groupingAttributes,
            final Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations, final boolean fast) {
        super(inputSchema, outputSchema, groupingAttributes, aggregations, fast);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tuple<T> createOutputElement(final Object groupID, final PairMap<SDFSchema, AggregateFunction, Tuple<T>, ?> r) {
        final ProbabilisticTuple<T> returnTuple = new ProbabilisticTuple<T>(this.getOutputSchema().size(), 1, true);
        final MultivariateMixtureDistribution[] tmpDistributions = new MultivariateMixtureDistribution[this.getOutputSchema().size()];
        int distributionIndex = 0;
        for (final Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, Tuple<T>> e : r.entrySet()) {
            final int pos = this.getOutputPos(e.getKey());
            if (e.getValue().getAttribute(0).getClass() == ProbabilisticDouble.class) {
                final MultivariateMixtureDistribution distribution = ((ProbabilisticTuple<T>) e.getValue()).getDistribution(0);
                distribution.setAttribute(0, pos);
                tmpDistributions[distributionIndex] = distribution;
                returnTuple.setAttribute(pos, new ProbabilisticDouble(distributionIndex));

                distributionIndex++;
            }
            else {
                returnTuple.setAttribute(pos, e.getValue().getAttribute(0));
            }
        }
        final MultivariateMixtureDistribution[] distributions = new MultivariateMixtureDistribution[distributionIndex];
        System.arraycopy(tmpDistributions, 0, distributions, 0, distributionIndex);
        returnTuple.setDistributions(distributions);
        this.addGroupingAttributes(groupID, returnTuple);
        return returnTuple;
    }

    @Override
    public Tuple<T> createOutputElement2(final Object groupID, final PairMap<SDFSchema, AggregateFunction, IPartialAggregate<Tuple<T>>, ?> r) {
        final ProbabilisticTuple<T> returnTuple = new ProbabilisticTuple<T>(this.getOutputSchema().size(), this.getOutputSchema().size(), true);

        for (final Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, IPartialAggregate<Tuple<T>>> e : r.entrySet()) {
            final int pos = this.getOutputPos(e.getKey());
            returnTuple.setAttribute(pos, e.getValue());
        }

        this.addGroupingAttributes(groupID, returnTuple);
        return returnTuple;
    }
    
}
