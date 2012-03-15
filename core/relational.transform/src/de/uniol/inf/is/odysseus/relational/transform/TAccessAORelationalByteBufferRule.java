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
package de.uniol.inf.is.odysseus.relational.transform;

import java.io.IOException;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ObjectHandler;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.RouterConnection;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.SizeByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleDataHandler;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAORelationalByteBufferRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration transformConfig) {
		String accessPOName = accessAO.getSourcename();
		SizeByteBufferReceiverPO accessPO = null;
		try {
			accessPO = new SizeByteBufferReceiverPO(new ObjectHandler(new RelationalTupleDataHandler(accessAO.getOutputSchema())), new RouterConnection(accessAO.getHost(), accessAO.getPort(),accessAO.isAutoReconnectEnabled(), accessAO.getLogin(), accessAO.getPassword()));
		} catch (IOException e) {			
			e.printStackTrace();
		}
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		getDataDictionary().putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(accessAO);
		insert(accessPO);
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration transformConfig) {
		if(getDataDictionary().getAccessPlan(accessAO.getSourcename()) == null){
			if(accessAO.getAdapter().equals("RelationalByteBufferAccessPO")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {		
		return "AccessAO (RelationalByteBuffer) -> AccessPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<?> getConditionClass() {	
		return AccessAO.class;
	}
	
}
