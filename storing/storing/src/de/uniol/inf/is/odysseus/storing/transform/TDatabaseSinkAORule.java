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

import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.storing.physicaloperator.DatabaseSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatabaseSinkAORule extends AbstractTransformationRule<DatabaseSinkAO>{	
	
	private boolean saveMetadata = false;
	
	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(DatabaseSinkAO databaseSinkAO, TransformationConfiguration tConfig) {		
		DatabaseSinkPO databaseSinkPO = new DatabaseSinkPO(databaseSinkAO.getConnection(), databaseSinkAO.getTable(), databaseSinkAO.isSavemetadata(), databaseSinkAO.isCreate(), databaseSinkAO.isIfnotexists(), databaseSinkAO.isTruncate());
		databaseSinkPO.setOutputSchema(databaseSinkAO.getOutputSchema());
		

		replace(databaseSinkAO,databaseSinkPO,tConfig);
		retract(databaseSinkAO);
		
		
		
	}

	@Override
	public boolean isExecutable(DatabaseSinkAO databaseSinkAO, TransformationConfiguration tConfig) {
		return databaseSinkAO.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		if(saveMetadata){
			return "DatabaseSinkAO -> RelationalTimestampToPayloadPO + DatabaseSinkPO";
		}
		return "DatabaseSinkAO -> DatabaseSinkPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
