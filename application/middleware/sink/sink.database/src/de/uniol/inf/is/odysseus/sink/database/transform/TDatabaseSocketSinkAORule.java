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

package de.uniol.inf.is.odysseus.sink.database.transform;

import de.uniol.inf.is.odysseus.datadictionary.WrapperPlanFactory;
import de.uniol.inf.is.odysseus.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sink.database.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.sink.database.physicaloperator.DatabaseSinkPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * 
 * @author Dennis Geesen Created at: 22.08.2011
 */
public class TDatabaseSocketSinkAORule extends AbstractTransformationRule<DatabaseSinkAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DatabaseSinkAO operator, TransformationConfiguration config) {
		ISink<?> sinkPO = WrapperPlanFactory.getSink(operator.getSinkName());

		if (sinkPO == null) {
			sinkPO = new DatabaseSinkPO(operator.getSinkName(), operator.getDatabasetype(), operator.getHost(), operator.getPort(), operator.getDatabasename(), operator.getTablename(), operator.getUser(), operator.getPassword());			
			sinkPO.setOutputSchema(operator.getOutputSchema());
			WrapperPlanFactory.putSink(operator.getSinkName(), sinkPO);
		}
		replace(operator, sinkPO, config);		
		retract(operator);		
	}

	@Override
	public boolean isExecutable(DatabaseSinkAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DatabaseSinkAO -> DatabaseSinkPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<?> getConditionClass() {
		return DatabaseSinkAO.class;
	}

}
