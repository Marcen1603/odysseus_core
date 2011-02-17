/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAOExistsRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 10;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.TRACE,"Transform AccessAO: " + accessAO);	
		ISource<?> accessPO = WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI());
		LoggerSystem.printlog(Accuracy.TRACE, "Transform to existing AccessPO: trafo = " + trafo);
		LoggerSystem.printlog(Accuracy.TRACE, "Transform to existing AccessPO: trafoHelper = " + trafo.getTransformationHelper());
		replace(accessAO, accessPO, trafo);		
		insert(accessPO);
		retract(accessAO);
		
	}

	@Override
	public boolean isExecutable(AccessAO operator, TransformationConfiguration transformConfig) {		
		return WrapperPlanFactory.getAccessPlan(operator.getSource().getURI(false)) != null;
	}

	@Override
	public String getName() {
		return "Transform to existing AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
}
