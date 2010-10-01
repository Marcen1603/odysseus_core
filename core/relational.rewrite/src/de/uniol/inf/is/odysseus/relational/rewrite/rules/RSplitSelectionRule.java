package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
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
		// Neuen SelectAO erstellen
		SelectAO newSel = new SelectAO();

		// AndPredicate splitten und verteilen
		AndPredicate andPred = (AndPredicate) sel.getPredicate();
		newSel.setPredicate(andPred.getLeft());
		sel.setPredicate(andPred.getRight());

		// Den neuen SelectAO als inputAO der alten setzen
		// Reihenfolge??
		RestructHelper.insertOperator(newSel, sel, 0, 0, 0);

		// neuen SelectAO einfügen, anderen updaten
		insert(newSel);
		update(sel);
	}

	@Override
	public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
		if (operator.getPredicate() != null) {
			if (operator.getPredicate() instanceof AndPredicate) {
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
