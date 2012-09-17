/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PunctuationAO;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.PunctuationPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPunctuationAORule extends AbstractTransformationRule<PunctuationAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(PunctuationAO punctuationAO, TransformationConfiguration config) {
        PunctuationPO<IMetaAttributeContainer<ITimeInterval>> punctuationPO = new PunctuationPO<IMetaAttributeContainer<ITimeInterval>>(punctuationAO.getRatio());
        defaultExecute(punctuationAO, punctuationPO, config, true, true);
    }

    @Override
    public boolean isExecutable(PunctuationAO operator, TransformationConfiguration config) {
        if (config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "PunctuationAO -> PunctuationPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super PunctuationAO> getConditionClass() {
    	return PunctuationAO.class;
    }
    
}
