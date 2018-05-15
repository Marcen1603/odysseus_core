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
package de.uniol.inf.is.odysseus.probabilistic.base.predicate;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractExpressionBuilder;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@SuppressWarnings("rawtypes")
public class ProbabilisticRelationalPredicateBuilder<M extends IMetaAttribute> extends AbstractExpressionBuilder {

    @Override
    public IPredicate<?> createPredicate(final IAttributeResolver resolver, final String predicate) {
        final SDFProbabilisticExpression expression = new SDFProbabilisticExpression("", predicate, resolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
        final ProbabilisticRelationalPredicate pred = new ProbabilisticRelationalPredicate(expression);
        return pred;
    }
    

    @Override
    public String getName() {
    	return "PROBABILISTICRELATIONALPREDICATE";
    }
    
    @Override
    public String getAliasName() {
    	return ProbabilisticTuple.class.getName();
    }

}
