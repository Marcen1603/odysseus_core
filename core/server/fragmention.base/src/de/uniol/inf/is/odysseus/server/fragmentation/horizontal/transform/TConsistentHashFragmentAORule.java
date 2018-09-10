/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ConsistentHashFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator.ConsistentHashFragmentPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * The rule of transformation for the {@link ConsistentHashFragmentAO}.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 */
public class TConsistentHashFragmentAORule extends AbstractTransformationRule<ConsistentHashFragmentAO> {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {
        return 0;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void execute(ConsistentHashFragmentAO fragmentAO, TransformationConfiguration config) throws RuleException {
        defaultExecute(fragmentAO, new ConsistentHashFragmentPO(fragmentAO), config, true, true);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(ConsistentHashFragmentAO fragmentAO, TransformationConfiguration transformConfig) {
        return fragmentAO.isAllPhysicalInputSet();
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "ConsistentHashFragmentAO -> ConsistentHashFragmentPO";
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public Class<? super ConsistentHashFragmentAO> getConditionClass() {
        return ConsistentHashFragmentAO.class;
    }

}