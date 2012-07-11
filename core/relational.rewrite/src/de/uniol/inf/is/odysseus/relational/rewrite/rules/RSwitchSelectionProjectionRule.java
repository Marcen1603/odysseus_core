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
package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchSelectionProjectionRule extends AbstractRewriteRule<ProjectAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(ProjectAO proj, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, proj)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(sel, proj);
				for (ILogicalOperator o:toUpdate){
					update(o);
				}

				update(proj);
				update(sel);
			}
		}

	}


	@Override
	public boolean isExecutable(ProjectAO proj, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, proj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Switch Selection and Projection";
	}
	

	private static boolean isValidSelect(SelectAO sel, ProjectAO proj) {
		if (sel.getInputAO().equals(proj)) {
			return true;
		}		
		return false;
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}
	
	@Override
	public Class<? super ProjectAO> getConditionClass() {	
		return ProjectAO.class;
	}

}
