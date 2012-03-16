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

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.ObjectDataHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational.base.Tuple;
import de.uniol.inf.is.odysseus.relational.base.access.ObjectInputStreamAccessPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem;
import de.uniol.inf.is.odysseus.ruleengine.system.LoggerSystem.Accuracy;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAccessAORelationalInputRule extends AbstractTransformationRule<AccessAO> {

	@Override
	public int getPriority() {		
		return 1;
	}

	@Override
	public void execute(AccessAO accessAO, TransformationConfiguration trafo) {
		LoggerSystem.printlog(Accuracy.DEBUG, "Standard InputStream");
		String accessPOName = accessAO.getSourcename();
		ISource<?> accessPO = new ObjectInputStreamAccessPO<Tuple<?>>(accessAO.getHost(), accessAO.getPort(), accessAO.getOutputSchema(), new ObjectDataHandler<Tuple<?>>() , accessAO.getLogin(), accessAO.getPassword());	
		accessPO.setOutputSchema(accessAO.getOutputSchema());
		getDataDictionary().putAccessPlan(accessPOName, accessPO);
		Collection<ILogicalOperator> toUpdate = trafo.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		insert(accessPO);
		retract(accessAO);
	}

	@Override
	public boolean isExecutable(AccessAO accessAO, TransformationConfiguration trafo) {
		if(getDataDictionary().getAccessPlan(accessAO.getSourcename()) == null){
			if(accessAO.getAdapter().equals("RelationalInputStreamAccessPO")){
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "AccessAO (RelationalInputStream) -> AccessPO";
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
