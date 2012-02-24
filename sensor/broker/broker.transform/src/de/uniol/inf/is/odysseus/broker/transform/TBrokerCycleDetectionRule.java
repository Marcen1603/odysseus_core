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

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerPO;
import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerWrapperPlanFactory;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionDetector;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerCycleDetectionRule extends AbstractTransformationRule<BrokerPO<?>> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(BrokerPO<?> operator, TransformationConfiguration transformConfig) {		
		LoggerSystem.printlog(Accuracy.DEBUG, "Searching for cycles and reorganize broker transactions..."); 
		new TransactionDetector().reorganizeTransactions(BrokerWrapperPlanFactory.getAllBrokerPOs());		
		LoggerSystem.printlog(Accuracy.DEBUG, "Searching done"); 
		retract(operator);
	}

	@Override
	public boolean isExecutable(BrokerPO<?> operator, TransformationConfiguration transformConfig) {
		return (!BrokerWrapperPlanFactory.isEmpty());
	}

	@Override
	public String getName() {
		return "Detecting Cycles Rule";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.CLEANUP;
	}


}
