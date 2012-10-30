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

import java.util.List;

import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.relational.base.Relational;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

@SuppressWarnings({"rawtypes"})
public class RSplitSelectionRule extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 4;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration config) {
		List<IPredicate> preds = null;
		// AndPredicate splitten und verteilen
		if (ComplexPredicateHelper.isAndPredicate(sel.getPredicate())) {
			preds = ComplexPredicateHelper.splitPredicate(sel.getPredicate());
		} else { // RelationalPredicate with and
			preds = ((RelationalPredicate)sel.getPredicate()).splitPredicate();
		}
		for (int i = 0; i < preds.size() - 1; i++) {
			// Neuen SelectAO erstellen
			SelectAO newSel = new SelectAO();
			for (IOperatorOwner owner:sel.getOwner()){
				newSel.addOwner(owner);
			}
			newSel.setPredicate(preds.get(i));
			// Den neuen SelectAO als inputAO der alten setzen
			// Reihenfolge??
			RestructHelper.insertOperator(newSel, sel, 0, 0, 0);
			insert(newSel);
		}
		sel.setPredicate(preds.get(preds.size() - 1));
		// neuen SelectAO einfügen, anderen updaten
		update(sel);
	}

	@Override
	public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
		if (!config.getQueryBuildConfiguration().getTransformationConfiguration().getDataType().equals(Relational.RELATIONAL)){
			return false;
		}
		IPredicate pred = operator.getPredicate();
		if (pred != null) {
			if (ComplexPredicateHelper.isAndPredicate(pred)
					|| (pred instanceof RelationalPredicate && ((RelationalPredicate) pred)
							.isAndPredicate())) {
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
	
	@Override
	public Class<? super SelectAO> getConditionClass() {	
		return SelectAO.class;
	}

}
