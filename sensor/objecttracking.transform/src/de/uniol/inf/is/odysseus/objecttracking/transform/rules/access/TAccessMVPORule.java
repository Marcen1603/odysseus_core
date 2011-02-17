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
package de.uniol.inf.is.odysseus.objecttracking.transform.rules.access;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AtomicDataInputStreamAccessMVPO;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessMVPORule extends AbstractTransformationRule<AccessAO>{

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void execute(AccessAO operator, TransformationConfiguration config) {
		System.out.println("CREATE AccessMVPO: " + operator); 
		String sourceName = operator.getSource().getURI(false);
		ISource accessPO = new AtomicDataInputStreamAccessMVPO(operator.getHost(), operator.getPort(), operator.getOutputSchema());
		accessPO.setOutputSchema(operator.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(sourceName, accessPO);
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, accessPO);

		for(ILogicalOperator o: toUpdate){
			update(o);
		}		

		retract(operator);
		insert(accessPO);
		System.out.println("CREATE AccessMVPO finished.");
	}

	@Override
	public boolean isExecutable(AccessAO operator,
			TransformationConfiguration config) {
		if(operator.getSourceType().equals("RelationalAtomicDataInputStreamAccessMVPO") &&
				WrapperPlanFactory.getAccessPlan(operator.getSource().getURI()) == null){
			return true;
		}
		
		return false;
		
		// DRL-Code
//		$accessAO : AccessAO( sourceType == "RelationalAtomicDataInputStreamAccessMVPO" )
//		$trafo: TransformationConfiguration()
//		eval(WrapperPlanFactory.getAccessPlan($accessAO.getSource().getURI()) == null)
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "AccessAO (AtomicData MV) -> AccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		// TODO Auto-generated method stub
		return TransformRuleFlowGroup.ACCESS;
	}

}
