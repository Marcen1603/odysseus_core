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

import de.uniol.inf.is.odysseus.core.server.logicaloperator.CommandAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.CommandPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This rule handles the command operator
 * 
 * @author Henrik Surm
 */
public class TCommandAORule extends AbstractTransformationRule<CommandAO> 
{

	@Override
	public void execute(CommandAO operator, TransformationConfiguration config) throws RuleException {
		IServerExecutor executor = config.getOption(IServerExecutor.class.getName());
		if (executor == null){
			throw new TransformationException("Cannot create PlanModificationActionPO. Executor not set in Transformation Configuration!");
		}
		
		CommandPO po = new CommandPO(operator, executor); 
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(CommandAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}	
	
	@Override
	public String getName() {
		return "CommandAO --> CommandPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super CommandAO> getConditionClass() {
		return CommandAO.class;
	}
}
