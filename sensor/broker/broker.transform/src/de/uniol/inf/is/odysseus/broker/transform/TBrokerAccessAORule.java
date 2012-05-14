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

import de.uniol.inf.is.odysseus.broker.physicaloperator.BrokerByteBufferHandler;
import de.uniol.inf.is.odysseus.core.connection.NioConnectionHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.push.ReceiverPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

@SuppressWarnings({"unchecked","rawtypes"})
public class TBrokerAccessAORule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG,"Using Broker Access Operator");
		String accessPOName = accessAO.getSourcename();
		ISource<?> accessPO = null;

		try {
			NioConnectionHandler accessHandler = new NioConnectionHandler(accessAO.getHost(), accessAO.getPort(),accessAO.isAutoReconnectEnabled(), accessAO.getLogin(), accessAO.getPassword());
			accessPO = new ReceiverPO(new ByteBufferHandler(new TupleDataHandler(accessAO.getOutputSchema())), new BrokerByteBufferHandler(), accessHandler);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		accessPO.setOutputSchema(accessAO.getOutputSchema());
		getDataDictionary().putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(accessAO);
		insert(accessPO);

	}

	@Override
	public boolean isExecutable(AccessAO operator, TransformationConfiguration transformConfig) {
		if (operator.getAdapter().equals("RelationalByteBufferAccessPO")) {
			if (transformConfig.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
				if (transformConfig.getOption("IBrokerInterval") != null) {
					if (getDataDictionary().getAccessPlan(operator.getSourcename()) == null) {
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
