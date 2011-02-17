/** Copyright [2011] [The Odysseus Team]
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

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchProjectionWindowRule extends AbstractRewriteRule<WindowAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(WindowAO win, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(win)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(proj, win);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(win);
				update(proj);
			}
		}
	}

	@Override
	public boolean isExecutable(WindowAO win, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(win)) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String getName() {
		return "Switch Projection and Window";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}
