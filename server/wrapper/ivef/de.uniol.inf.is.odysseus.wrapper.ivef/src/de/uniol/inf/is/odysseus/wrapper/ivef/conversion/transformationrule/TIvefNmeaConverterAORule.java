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
package de.uniol.inf.is.odysseus.wrapper.ivef.conversion.transformationrule;

import de.uniol.inf.is.odysseus.wrapper.ivef.conversion.logicaloperator.IvefNmeaConverterAO;
import de.uniol.inf.is.odysseus.wrapper.ivef.conversion.physicaloperator.IvefNmeaConverterPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TIvefNmeaConverterAORule extends AbstractTransformationRule<IvefNmeaConverterAO> {

	@Override
	public int getPriority() {
		return 0;
	}
	
	@Override
	public void execute(IvefNmeaConverterAO converterAO, TransformationConfiguration config) throws RuleException {		
		defaultExecute(converterAO, new IvefNmeaConverterPO<>(converterAO.getConversionType(), converterAO.getPositionToStaticRatio()), config, true, true);
	}

	@Override
	public boolean isExecutable(IvefNmeaConverterAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet(); 
	}

	@Override
	public String getName() {
		return "IvefNmeaConverterAO -> IvefNmeaConverterPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super IvefNmeaConverterAO> getConditionClass() {	
		return IvefNmeaConverterAO.class;
	}

}
