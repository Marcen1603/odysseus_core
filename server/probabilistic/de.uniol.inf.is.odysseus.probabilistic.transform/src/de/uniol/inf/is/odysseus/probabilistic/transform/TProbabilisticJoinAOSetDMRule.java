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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticJoinAOSetDMRule extends AbstractTransformationRule<JoinTIPO<ITimeInterval, Tuple<ITimeInterval>>> {

    @Override
    public int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void execute(final JoinTIPO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        final IDataMergeFunction<Tuple<IProbabilisticTimeInterval>, IProbabilisticTimeInterval> dataMerge = new ProbabilisticMergeFunction<Tuple<IProbabilisticTimeInterval>, IProbabilisticTimeInterval>(
                operator.getOutputSchema().size());
        operator.setDataMerge(dataMerge);
        this.update(operator);
    }

    @Override
    public boolean isExecutable(final JoinTIPO<ITimeInterval, Tuple<ITimeInterval>> operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getOutputSchema());
        Objects.requireNonNull(transformConfig);
        if (operator.getOutputSchema().getType() == ProbabilisticTuple.class) {
            if (operator.getDataMerge() == null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "Insert DataMergeFunction JoinTIPO (Probabilistic)";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.METAOBJECTS;
    }

    @Override
    public Class<? super JoinTIPO<?, ?>> getConditionClass() {
        return JoinTIPO.class;
    }

}
