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
package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.SetSystemTimeAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SetSystemTimePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSetSystemTimeAORule extends AbstractTransformationRule<SetSystemTimeAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(SetSystemTimeAO systemTimeAO, TransformationConfiguration transformConfig) throws RuleException {		
		@SuppressWarnings("rawtypes")
		SetSystemTimePO<?> setSystemTimePO = new SetSystemTimePO(systemTimeAO.getThreshold());
		defaultExecute(systemTimeAO, setSystemTimePO, transformConfig, true, true);		
	}

	@Override
	public boolean isExecutable(SetSystemTimeAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SetSystemTimeAO -> SetSystemTimePO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super SetSystemTimeAO> getConditionClass() {	
		return SetSystemTimeAO.class;
	}

}
