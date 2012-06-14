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
package de.uniol.inf.is.odysseus.scars.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.jdveaccess.ao.JDVEAccessMVAO;
import de.uniol.inf.is.odysseus.scars.operator.jdveaccess.po.JDVEAccessMVPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
@SuppressWarnings({"rawtypes"})
public class TJDVEAccessMVPOAsListRule extends AbstractTransformationRule<AccessAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		String accessPOName = operator.getSourcename();
		AbstractSensorAccessPO<?, ?> accessPO = null;
		System.out.println("Host = " + operator.getHost());
			
		accessPO = new JDVEAccessMVPO(operator.getPort());
		System.out.println("JDVEAccessMVPO created");
			
		accessPO.setObjectListPath(((JDVEAccessMVAO) operator).getObjectListPath());
		
		accessPO.setOutputSchema(operator.getOutputSchema());
		getDataDictionary().putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(operator);
	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		if(operator.getWrapper().equals("JDVEAccessMVPO") &&
				getDataDictionary().getAccessPlan(operator.getSourcename()) == null){
			return true;
		}
		
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO -> JDVEAccessMVPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.ACCESS;
	}

}
