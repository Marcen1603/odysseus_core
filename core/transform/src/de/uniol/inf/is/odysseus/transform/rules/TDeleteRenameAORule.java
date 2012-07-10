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

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDeleteRenameAORule extends AbstractTransformationRule<RenameAO>{

	@Override
	public int getPriority() {		
		return 1;
	}

	@Override
	public void execute(RenameAO rename, TransformationConfiguration transformConfig) {		
		LoggerFactory.getLogger(TDeleteRenameAORule.class).debug("RenameAO removing...: " + rename);
		Collection<ILogicalOperator> toUpdate = RestructHelper.removeOperator(rename, true);		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(rename);
		LoggerFactory.getLogger(TDeleteRenameAORule.class).debug("RenameAO removed: " + rename);
	}

	@Override
	public boolean isExecutable(RenameAO operator, TransformationConfiguration transformConfig) {	
		// Remove only if child not rename. Do not remove top renaming
		return !(operator.getInputAO() instanceof RenameAO) && 
			   !(operator.getSubscriptions().size() == 1 && operator.getSubscriptions().iterator().next().getTarget() instanceof TopAO) &&
			   !(operator.getSubscriptions().size() == 0);
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
	public Class<?> getConditionClass() {	
		return RenameAO.class;
	}
}
