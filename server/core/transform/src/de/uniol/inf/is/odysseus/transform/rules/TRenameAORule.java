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
package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.TopPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRenameAORule extends AbstractTransformationRule<RenameAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(RenameAO operator, TransformationConfiguration config) throws RuleException {
		if (operator.getName().equalsIgnoreCase("Rename") && !isLastOne(operator)){
			operator.setName("TOP");
		}
		defaultExecute(operator, new TopPO<IStreamObject<?>>(), config ,true, true);
	}

	@Override
	public boolean isExecutable(RenameAO operator,
			TransformationConfiguration config) {
		// TODO: what are these rename and keyvalue things
		return //operator.getInputSchema().getType() != KeyValueObject.class &&
				operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "RenameAO --> TopPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super RenameAO> getConditionClass() {
		return RenameAO.class;
	}

}
