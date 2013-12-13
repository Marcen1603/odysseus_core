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

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.ProbabilisticContinuousSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.server.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.transform.rules.TSelectAORule;

/**
 * Transformation rule for Select operator for continuous probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisiticContinuousSelectAORule extends TSelectAORule {
    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public final void execute(final SelectAO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(transformConfig);
         final IPhysicalOperator selectPO = new ProbabilisticContinuousSelectPO(operator.getInputSchema(), operator.getPredicate());
        if (operator.getHeartbeatRate() > 0) {
            ((ProbabilisticDiscreteSelectPO<?>) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(operator.getHeartbeatRate()));
        }
        this.defaultExecute(operator, selectPO, transformConfig, true, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public final boolean isExecutable(final SelectAO operator, final TransformationConfiguration transformConfig) {
        Objects.requireNonNull(operator);
        Objects.requireNonNull(operator.getInputSchema());
        Objects.requireNonNull(transformConfig); 
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                if (SchemaUtils.containsContinuousProbabilisticAttributes(PredicateUtils.getAttributes(operator.getPredicate()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public final String getName() {
        return "SelectAO -> ProbabilisticDiscreteSelectPO";
    }

}
