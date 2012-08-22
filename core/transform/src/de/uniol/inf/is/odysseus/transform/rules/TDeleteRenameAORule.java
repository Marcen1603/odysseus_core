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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDeleteRenameAORule extends AbstractTransformationRule<RenameAO> {

	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void execute(RenameAO rename, TransformationConfiguration transformConfig) {		
		Collection<ILogicalOperator> toUpdate = RestructHelper.removeOperator(rename, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(rename);		
	}

	@Override
	public boolean isExecutable(RenameAO operator, TransformationConfiguration transformConfig) { 
		// Remove only if child not rename. Do not remove top renaming, when schemas are different 
		return (!isInputRenameAO(operator) && !(isLastOne(operator) && !schemaEquals(operator)) && !hasSupscriptions(operator));
	}

	private boolean schemaEquals(RenameAO operator) {
		if(operator.getInputSchema().equals(operator.getOutputSchema())){
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

	private boolean isLastOne(RenameAO operator) {
		if (operator.getSubscriptions().size() == 1) {
			if (operator.getSubscriptions().iterator().next().getTarget() instanceof TopAO) {
				return true;
			}
		}
		return false;
	}

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
