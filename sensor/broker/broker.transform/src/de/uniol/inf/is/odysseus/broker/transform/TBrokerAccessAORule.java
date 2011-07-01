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

import java.io.IOException;
import java.util.Collection;

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.ObjectHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleDataHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TBrokerAccessAORule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG,"Using Broker Access Operator");
		String accessPOName = accessAO.getSource().getURI(false);
		ISource<?> accessPO = null;

		try {
			accessPO = new BrokerByteBufferReceiverPO(new ObjectHandler(new RelationalTupleDataHandler(accessAO.getOutputSchema())), accessAO.getHost(), accessAO.getPort());
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		accessPO.setOutputSchema(accessAO.getOutputSchema());
		WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(accessAO);
		insert(accessPO);

	}

	@Override
	public boolean isExecutable(AccessAO operator, TransformationConfiguration transformConfig) {
		if (operator.getSourceType().equals("RelationalByteBufferAccessPO")) {
			if (transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
				if (transformConfig.getOption("IBrokerInterval") != null) {
					if (WrapperPlanFactory.getAccessPlan(operator.getSource().getURI()) == null) {
						return true;
					}
				}

			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (RelationalByteBuffer) -> BrokerAccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
	    return TransformRuleFlowGroup.ACCESS;
	}

}
