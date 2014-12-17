package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public abstract class AbstractSwitchSelectionRule<Operator extends ILogicalOperator> extends AbstractRewriteRule<Operator> {

	@Override
	public int getPriority() {
		return 40;
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}
	
	protected boolean isValidSelect(SelectAO sel, Operator op) {
		if (sel.getInputAO() != null && sel.getInputAO().equals(op)) {
			return true;
		}
		return false;
	}
	
}