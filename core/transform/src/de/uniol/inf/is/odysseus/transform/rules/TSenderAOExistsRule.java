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

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSenderAOExistsRule extends AbstractTransformationRule<SenderAO> {

	@Override
	public int getPriority() {
		return 100;
	}

	@Override
	public void execute(SenderAO senderAO, TransformationConfiguration trafo) {		
		ISink<?> sinkPO = getDataDictionary().getSinkplan(senderAO.getSinkname());		
		defaultExecute(senderAO, sinkPO, trafo, true, true);
		sinkPO.setName(senderAO.getSinkname());
	}

	@Override
	public boolean isExecutable(SenderAO operator, TransformationConfiguration transformConfig) {		
		return getDataDictionary().getSinkplan(operator.getSinkname()) != null;
	}

	@Override
	public String getName() {
		return "Transform to existing SenderPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SENDER;
	}
	
	@Override
	public Class<? super SenderAO> getConditionClass() {	
		return SenderAO.class;
	}
}
