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

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSplitSelectionRule extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 4;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration config) {

		// AndPredicate splitten und verteilen
		List<IPredicate> preds = ComplexPredicateHelper.splitPredicate(sel.getPredicate());
		for (int i=0; i< preds.size()-1;i++){
			// Neuen SelectAO erstellen
			SelectAO newSel = new SelectAO();
			newSel.setPredicate(preds.get(i));
			// Den neuen SelectAO als inputAO der alten setzen
			// Reihenfolge??
			RestructHelper.insertOperator(newSel, sel, 0, 0, 0);
			insert(newSel);
		}
		sel.setPredicate(preds.get(preds.size()-1));
		// neuen SelectAO einf�gen, anderen updaten
		update(sel);
	}

	@Override
	public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
		if (operator.getPredicate() != null) {
			if (ComplexPredicateHelper.isAndPredicate(operator.getPredicate())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Split Selection with more than one Predicate";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SPLIT;
	}

}
