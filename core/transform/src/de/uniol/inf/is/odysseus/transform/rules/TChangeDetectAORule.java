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
package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;



public class TChangeDetectAORule extends
		AbstractTransformationRule<ChangeDetectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ChangeDetectAO operator,
			TransformationConfiguration config) {
		ChangeDetectPO<?> po = new ChangeDetectPO<IMetaAttribute>();
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(ChangeDetectAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && !operator.hasAttributes();
	}

	@Override
	public String getName() {
		return "ChangeDetectAO -> ChangeDetectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super ChangeDetectAO> getConditionClass() {
		return ChangeDetectAO.class;
	}

}
