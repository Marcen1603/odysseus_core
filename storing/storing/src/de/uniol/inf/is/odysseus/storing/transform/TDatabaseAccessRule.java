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
package de.uniol.inf.is.odysseus.storing.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseAccessAO;
import de.uniol.inf.is.odysseus.storing.physicaloperator.DatabaseAccessPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatabaseAccessRule extends AbstractTransformationRule<DatabaseAccessAO>{

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(DatabaseAccessAO accessAO, TransformationConfiguration config) {
		String accessPOName = accessAO.getSource().getURI(false);		
		ISource<?> accessPO = null;	
		if(WrapperPlanFactory.getAccessPlan(accessAO.getSource().getURI()) == null){
			accessPO = new DatabaseAccessPO(accessAO.getTable(), accessAO.getConnection(), accessAO.isTimesensitiv());
			accessPO.setOutputSchema(accessAO.getOutputSchema());
			WrapperPlanFactory.putAccessPlan(accessPOName, accessPO);			
		}else{
			accessPO = WrapperPlanFactory.getAccessPlan(accessPOName);
		}
		
		
		Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(accessAO, accessPO);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(accessAO);
		insert(accessPO);		
	}

	@Override
	public boolean isExecutable(DatabaseAccessAO accessAO, TransformationConfiguration config) {		
		return true;					
	}

	@Override
	public String getName() {
		return "DatabaseAccessAO -> DatabaseAccessPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}
	

}
