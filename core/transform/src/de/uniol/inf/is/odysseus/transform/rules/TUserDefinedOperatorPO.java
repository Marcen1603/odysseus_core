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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UserDefinedOperatorAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.UserDefinedOperatorPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUserDefinedOperatorPO extends
		AbstractTransformationRule<UserDefinedOperatorAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(UserDefinedOperatorAO operator,
			TransformationConfiguration config) {
		UserDefinedOperatorPO po = new UserDefinedOperatorPO();
		po.setOutputSchema(operator.getOutputSchema());
		po.setInitString(operator.getInitString());
		po.setUdf(operator.getUdf());		
		replace(operator, po, config);		
		retract(operator);
	}

	@Override
	public boolean isExecutable(UserDefinedOperatorAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "UdoAO -> UdoPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {
		return UserDefinedOperatorAO.class;
	}

}
