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
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RMergeSelectionRule extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SelectAO operator, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (sel.getInputAO()== operator) {
				if (sel.getPredicate() != null) {
					if (operator.getPredicate() != null) {
						operator.setPredicate(ComplexPredicateHelper.createAndPredicate(operator.getPredicate(), sel.getPredicate()));
					} else {
						operator.setPredicate(sel.getPredicate());
					}
					Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(sel);
					for (ILogicalOperator o : toUpdate) {
						update(o);
					}
					update(operator);
					retract(sel);
				}
			}
		}		
	}

	
	@Override
	public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (sel.getInputAO()== operator) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Merge Selection";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.CLEANUP;
	}
	
	@Override
	public Class<?> getConditionClass() {	
		return SelectAO.class;
	}

}
