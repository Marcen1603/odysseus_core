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
package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDeleteRenameAORule extends AbstractTransformationRule<RenameAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(RenameAO rename, TransformationConfiguration transformConfig) throws RuleException {
		Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(rename, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(rename);
	}

	@Override
	public boolean isExecutable(RenameAO operator, TransformationConfiguration transformConfig) {
		// Remove only if child not rename. Do not remove top renaming, when schemas are different
		return !operator.isNoOp() && (!isInputRenameAO(operator) && !(isLastOne(operator) && !schemaEquals(operator)) && !hasSupscriptions(operator)
				// TODO: what are these rename and keyvalue things
				//&& !isKeyValue(operator)
				);
	}

	private boolean schemaEquals(RenameAO operator) {
		SDFSchema input = operator.getInputSchema();
		SDFSchema output = operator.getOutputSchema();
		if(input.equals(output)){
			return true;
		}
		return false;
	}

	private boolean isInputRenameAO(RenameAO operator) {
		return (operator.getInputAO() instanceof RenameAO);
	}

	private boolean hasSupscriptions(RenameAO operator) {
		return (operator.getSubscriptions().size() == 0);
	}

//	private boolean isKeyValue(RenameAO operator) {
//		return (operator.getInputSchema().getType() == KeyValueObject.class );
//	}

	@Override
	public String getName() {
		return "Delete Rename";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public Class<? super RenameAO> getConditionClass() {
		return RenameAO.class;
	}
}
