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
package de.uniol.inf.is.odysseus.probabilistic.transform.discrete;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.server.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.transform.rules.TSelectAORule;

/**
 * Transformation rule for Select operator for discrete probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisiticDiscreteSelectAORule extends TSelectAORule {

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
    @Override
    public final void execute(final SelectAO selectAO, final TransformationConfiguration transformConfig) {
        final int[] probabilisticAttributePos = SchemaUtils.getAttributePos(selectAO.getInputSchema(), PredicateUtils.getAttributes(selectAO.getPredicate()));
        @SuppressWarnings({ "unchecked", "rawtypes" })
        final IPhysicalOperator selectPO = new ProbabilisticDiscreteSelectPO(selectAO.getPredicate(), probabilisticAttributePos);
        if (selectAO.getHeartbeatRate() > 0) {
            ((ProbabilisticDiscreteSelectPO<?>) selectPO).setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration<ITimeIntervalProbabilistic, ProbabilisticTuple<ITimeIntervalProbabilistic>>(
                    selectAO.getHeartbeatRate()));
        }
        this.defaultExecute(selectAO, selectPO, transformConfig, true, true);
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
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                if (SchemaUtils.containsDiscreteProbabilisticAttributes(PredicateUtils.getAttributes(operator.getPredicate()))) {
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
