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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CommandAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.CommandPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
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
	static Logger LOG = LoggerFactory.getLogger(TCommandAORule.class);
	static InfoService infoService = InfoServiceFactory.getInfoService(TCommandAORule.class);

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override public void execute(CommandAO operator, TransformationConfiguration config) throws RuleException 
	{
        CommandPO po = new CommandPO(operator.getCommand());
        
        for (String target : operator.getTargets())
        {
        	Resource id = new Resource(getCaller().getUser(), target);
        	IPhysicalOperator targetOperator = getDataDictionary().getOperator(id, getCaller());
        	if (targetOperator == null)
        		throw new RuleException("Target operator \"" + target + "\" doesn't exist or doesn't belong to current user!");
        	
        	if (targetOperator instanceof ICommandProvider)
        	{
        		po.addCommandListener((ICommandProvider)targetOperator, operator.getInputSchema());
        	}
        	else
        		throw new RuleException("Target operator \"" + target + "\" doesn't implement ICommandProvider!");
        }
        
        defaultExecute(operator, po, config, true, true);
	}

	@Override public boolean isExecutable(CommandAO operator, TransformationConfiguration config) 
	{
		return operator.isAllPhysicalInputSet();
	}	
	
	@Override
	public String getName() {
		return "CommandAO --> CommandPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SENDER;
	}

	@Override
	public Class<? super CommandAO> getConditionClass() {
		return CommandAO.class;
	}
}
