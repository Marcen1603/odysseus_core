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
package de.uniol.inf.is.odysseus.broker.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"rawtypes"})
public class TBrokerAORule extends AbstractTransformationRule<BrokerAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(BrokerAO brokerAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "CREATE Broker: " + brokerAO.getIdentifier()); 
		BrokerPO brokerPO = new BrokerPO(brokerAO.getIdentifier());
		brokerPO.setOutputSchema(brokerAO.getOutputSchema());
		brokerPO.setQueueSchema(brokerAO.getQueueSchema());
		BrokerWrapperPlanFactory.putPlan(brokerAO.getIdentifier(), brokerPO);			
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(brokerAO,brokerPO);							
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		
		retract(brokerAO);
		insert(brokerPO);
		LoggerSystem.printlog(Accuracy.DEBUG, "CREATE Broker end.");
		
	}

	@Override
	public boolean isExecutable(BrokerAO operator, TransformationConfiguration transformConfig) {
		return (BrokerWrapperPlanFactory.getPlan(operator.getIdentifier()) == null);
	}

	@Override
	public String getName() {
		return "BrokerAO -> BrokerPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.ACCESS;
	}

}
