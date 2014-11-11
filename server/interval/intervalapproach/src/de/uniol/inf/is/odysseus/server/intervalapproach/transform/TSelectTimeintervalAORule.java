/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.NElementHeartbeatGeneration;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TSelectTimeintervalAORule extends AbstractIntervalTransformationRule<SelectAO> {

	@Override
	public int getPriority() {		
		return 10;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SelectAO selectAO, TransformationConfiguration transformConfig) throws RuleException {	
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
		if (selectAO.getHeartbeatRate() > 0){
			selectPO.setHeartbeatGenerationStrategy(new NElementHeartbeatGeneration(
					selectAO.getHeartbeatRate()));
		}
		defaultExecute(selectAO, selectPO, transformConfig, true, true);
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super SelectAO> getConditionClass() {	
		return SelectAO.class;
	}

}
