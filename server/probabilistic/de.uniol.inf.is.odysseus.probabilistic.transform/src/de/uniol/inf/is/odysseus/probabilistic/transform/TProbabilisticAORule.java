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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.MetadataUpdatePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.logicaloperator.ProbabilisticAO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticFactory;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisticAORule extends AbstractTransformationRule<ProbabilisticAO> {
    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public final void execute(final ProbabilisticAO operator, final TransformationConfiguration config) {
        final int pos = operator.getInputSchema().indexOf(operator.getAttribute());
        final ProbabilisticFactory mUpdater = new ProbabilisticFactory(pos);
        final MetadataUpdatePO<?, ?> po = new MetadataUpdatePO<IProbabilistic, Tuple<? extends IProbabilistic>>(mUpdater);
        this.defaultExecute(operator, po, config, true, true);

    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public final boolean isExecutable(final ProbabilisticAO operator, final TransformationConfiguration config) {
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {
                return true;
            }
        }
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public final String getName() {
        return "ProbabilisticAO -> MetadataUpdatePO(probabilistic)";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
     */
    @Override
    public final IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
     */
    @Override
    public final Class<? super ProbabilisticAO> getConditionClass() {
        return ProbabilisticAO.class;
    }
}
