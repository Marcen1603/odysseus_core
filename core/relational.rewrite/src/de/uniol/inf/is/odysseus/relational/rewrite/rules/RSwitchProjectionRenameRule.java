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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchProjectionRenameRule extends AbstractRewriteRule<RenameAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(RenameAO rename, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(rename)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(proj, rename);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(rename);
				update(proj);
			}
		}
	}

	@Override
	public boolean isExecutable(RenameAO rename, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(rename)) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String getName() {
		return "Switch Projection and Rename";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}
	
	@Override
	public Class<? super RenameAO> getConditionClass() {	
		return RenameAO.class;
	}

}
