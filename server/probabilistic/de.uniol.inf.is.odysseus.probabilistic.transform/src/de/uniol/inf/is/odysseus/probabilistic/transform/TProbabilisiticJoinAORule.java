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
package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.interval.TITransferArea;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicate;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.server.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.transform.join.TJoinAORule;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TProbabilisiticJoinAORule extends TJoinAORule {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void execute(final JoinAO operator, final TransformationConfiguration config) throws RuleException {
        IPredicate pred = operator.getPredicate();
        if (pred == null) {
            super.execute(operator, config);
        }
        else {
            JoinTIPO joinPO = new JoinTIPO();
            boolean isCross = false;

            joinPO.setJoinPredicate(new ProbabilisticRelationalPredicate(getExpression(operator)));
            joinPO.setCardinalities(operator.getCard());

            joinPO.setTransferFunction(new TITransferArea());

            joinPO.setMetadataMerge(new CombinedMergeFunction());
            joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

            defaultExecute(operator, joinPO, config, true, true);
            if (isCross) {
                joinPO.setName("Crossproduct");
            }
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final JoinAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema(0));
        Objects.requireNonNull(operator.getInputSchema(1));
        Objects.requireNonNull(config);

        if (operator.isAllPhysicalInputSet()) {
            if ((operator.getInputSchema(0).getType() == ProbabilisticTuple.class) && (operator.getInputSchema(1).getType() == ProbabilisticTuple.class)) {
                return true;
            }
        }
        return super.isExecutable(operator, config);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return "JoinAO -> probabilistic Join";
    }

    private SDFExpression getExpression(final ILogicalOperator operator) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getPredicate());
        final String mepString = operator.getPredicate().toString();
        final SDFSchema leftInputSchema = operator.getInputSchema(0);
        final SDFSchema rightInputSchema = operator.getInputSchema(1);

        final SDFSchema inputSchema = SDFSchema.union(leftInputSchema, rightInputSchema);
        final IAttributeResolver attrRes = new DirectAttributeResolver(inputSchema);
        final SDFExpression expr = new SDFExpression(null, mepString, attrRes, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
        return expr;
    }
}
