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
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RMergeSelectionJoinRule extends AbstractRewriteRule<JoinAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinAO join, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelectAO(sel, join)) {
				if (sel.getPredicate() != null) {
					if (join.getPredicate() != null) {
						join.setPredicate(ComplexPredicateHelper.createAndPredicate(join.getPredicate(), sel.getPredicate()));
					} else {
						join.setPredicate(sel.getPredicate());
					}
					Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(sel);
					for (ILogicalOperator o : toUpdate) {
						update(o);
					}
					update(join);
					retract(sel);
				}
			}
		}

	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelectAO(sel, join)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Merge Selection Join";
	}

	private static boolean isValidSelectAO(SelectAO sel, JoinAO join) {
		if (sel.getPredicate() != null) {
			Set<?> sources = RelationalRestructHelper.sourcesOfPredicate(sel.getPredicate());
			ILogicalOperator left = join.getLeftInput();
			ILogicalOperator right = join.getRightInput();
			if (!RelationalRestructHelper.containsAllSources(left, sources)) {
				if (!RelationalRestructHelper.containsAllSources(right, sources)) {
					if (RelationalRestructHelper.containsAllSources(join, sources)) {
						if (sel.getPredicate() != null) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return JoinAO.class;
	}

}
