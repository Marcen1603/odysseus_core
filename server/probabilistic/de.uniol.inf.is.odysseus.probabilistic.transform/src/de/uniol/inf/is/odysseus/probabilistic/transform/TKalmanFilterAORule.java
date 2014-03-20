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

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.KalmanFilterAO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.KalmanFilterPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TKalmanFilterAORule extends AbstractTransformationRule<KalmanFilterAO> {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final int getPriority() {
        return 0;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final void execute(final KalmanFilterAO operator, final TransformationConfiguration config) throws RuleException {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(config);
        final IPhysicalOperator filterPO = new KalmanFilterPO<ITimeInterval>(operator.getInputSchema(), operator.determineAttributesList(), operator.getStateTransitionExpression().expression,
                operator.getControlExpression() != null ? operator.getControlExpression().expression : null, operator.getProcessNoiseExpression().expression,
                operator.getMeasurementExpression().expression, operator.getMeasurementNoiseExpression().expression, operator.getInitialState(), operator.getInitialError());
        this.defaultExecute(operator, filterPO, config, true, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final boolean isExecutable(final KalmanFilterAO operator, final TransformationConfiguration config) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(config);
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final String getName() {
        return "KalmanFilterAO -> KalmanFilterPO";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final Class<? super KalmanFilterAO> getConditionClass() {
        return KalmanFilterAO.class;
    }

}
