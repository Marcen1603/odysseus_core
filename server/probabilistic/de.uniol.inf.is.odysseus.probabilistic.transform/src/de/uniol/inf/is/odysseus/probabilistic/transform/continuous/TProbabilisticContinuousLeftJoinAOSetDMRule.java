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
package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import java.util.Objects;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalLeftMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.LeftJoinTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@SuppressWarnings({ "unused" })
public class TProbabilisticContinuousLeftJoinAOSetDMRule extends AbstractTransformationRule<LeftJoinTIPO<ITimeInterval, Tuple<ITimeInterval>>> {

    @Override
    public int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    @Override
    public void execute(final LeftJoinTIPO<ITimeInterval, Tuple<ITimeInterval>> operator, final TransformationConfiguration transformConfig) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema(0));
        Objects.requireNonNull(operator.getInputSchema(1));
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        operator.setDataMerge(new RelationalLeftMergeFunction<ITimeInterval>(operator.getInputSchema(0), operator.getInputSchema(1), operator.getOutputSchema()));
        this.update(operator);
    }

    @Override
    public boolean isExecutable(final LeftJoinTIPO<ITimeInterval, Tuple<ITimeInterval>> operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        if (operator.getOutputSchema().getType() == ProbabilisticTuple.class) {
            if (operator.getDataMerge() == null) {
                final IPredicate<?> predicate = operator.getPredicate();
                final Set<SDFAttribute> attributes = PredicateUtils.getAttributes(predicate);
                // if
                // (SchemaUtils.containsContinuousProbabilisticAttributes(attributes))
                // {
                // throw new TransformationException("Not implemented");
                //
                // }
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Insert DataMergeFunction (Probabilistic)";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    @Override
    public Class<? super LeftJoinTIPO<?, ?>> getConditionClass() {
        return LeftJoinTIPO.class;
    }

}
