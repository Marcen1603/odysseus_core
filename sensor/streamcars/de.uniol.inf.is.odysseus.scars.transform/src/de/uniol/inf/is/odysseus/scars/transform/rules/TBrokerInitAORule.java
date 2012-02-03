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

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.scars.operator.brokerinit.ao.BrokerInitAO;
import de.uniol.inf.is.odysseus.scars.operator.brokerinit.po.BrokerInitPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerInitAORule extends AbstractTransformationRule<BrokerInitAO> {

	@Override
	public int getPriority() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public void execute(BrokerInitAO operator,
			TransformationConfiguration config) {
		System.out.println("CREATE BrokerInitPO.");
		BrokerInitPO<?> po = new BrokerInitPO<Object>();
		
		po.setOutputSchema(operator.getOutputSchema());
		po.setSize(operator.getSize());
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		insert(po);
		retract(operator);
		System.out.println("CREATE BrokerInitPO finished.");
		
	}

	@Override
	public boolean isExecutable(BrokerInitAO operator,
			TransformationConfiguration config) {
		if(operator.isAllPhysicalInputSet()){
			return true;
		}
		return false;
		
		// DRL-Code
//		trafo : TransformationConfiguration()
//		$ao : BrokerInitAO( allPhysicalInputSet == true )
	}

	@Override
	public String getName() {
		return "BrokerInitAO -> BrokerInitPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
