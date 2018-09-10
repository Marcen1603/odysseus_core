/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.datarate.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.datarate.IDatarate;
import de.uniol.inf.is.odysseus.datarate.logicaloperator.DatarateAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDeleteDatarateAORule extends
		AbstractTransformationRule<DatarateAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(DatarateAO rename,
			TransformationConfiguration transformConfig) throws RuleException {
		Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(
				rename, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(rename);
	}

	@Override
	public boolean isExecutable(DatarateAO operator,
			TransformationConfiguration transformConfig) {
		// Remove if tranformConfig does not contain IDataRate
		return !operator.getInputSchema().hasMetatype(
				IDatarate.class);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public Class<? super DatarateAO> getConditionClass() {
		return DatarateAO.class;
	}
}
