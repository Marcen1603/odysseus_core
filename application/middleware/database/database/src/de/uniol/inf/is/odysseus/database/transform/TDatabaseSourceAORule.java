/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.database.transform;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSourceAO;
import de.uniol.inf.is.odysseus.database.physicaloperator.DatabaseSourcePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 */
public class TDatabaseSourceAORule extends AbstractTransformationRule<DatabaseSourceAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DatabaseSourceAO accessAO, TransformationConfiguration config) {
		String accessPOName = accessAO.getSourceName();	
		ISource<?> accessPO = null;		
		if (getDataDictionary().getAccessPlan(accessAO.getSourceName()) == null) {
			accessPO = new DatabaseSourcePO(accessAO.getTableName(), accessAO.getConnection(), accessAO.isTimesensitiv(), accessAO.getWaitMillis());
			accessPO.setOutputSchema(accessAO.getOutputSchema());
			getDataDictionary().putAccessPlan(accessPOName, accessPO);
		} else {
			accessPO = getDataDictionary().getAccessPlan(accessPOName);
		}

		replace(accessAO, accessPO, config);
		insert(accessPO);
		retract(accessAO);
	}

	@Override
	public boolean isExecutable(DatabaseSourceAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public String getName() {
		return "DatabaseSourceAO -> DatabaseSourcePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

	@Override
	public Class<?> getConditionClass() {
		return DatabaseSourceAO.class;
	}

}
