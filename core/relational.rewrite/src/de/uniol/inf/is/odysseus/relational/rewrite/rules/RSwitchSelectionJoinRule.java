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
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchSelectionJoinRule extends AbstractRewriteRule<JoinAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(JoinAO join, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, join)) {
				Collection<ILogicalOperator> toInsert = new TreeSet<ILogicalOperator>();
				Collection<ILogicalOperator> toRemove = new TreeSet<ILogicalOperator>();
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(sel, join, toInsert, toRemove);
				for (ILogicalOperator o : toInsert) {
					insert(o);
				}
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				for (ILogicalOperator o : toRemove) {
					retract(o);
				}
				update(join);
			}
		}

	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, join)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Switch Selection and Join";
	}

	private boolean isValidSelect(SelectAO sel, JoinAO join) {
		if (sel.getInputAO().equals(join)) {
			if (RelationalRestructHelper.subsetPredicate(sel.getPredicate(), join.getInputSchema(0))
					|| RelationalRestructHelper.subsetPredicate(sel.getPredicate(), join.getInputSchema(1))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}
}
