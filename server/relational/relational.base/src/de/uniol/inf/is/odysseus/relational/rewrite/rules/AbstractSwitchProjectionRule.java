package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public abstract class AbstractSwitchProjectionRule<Operator extends ILogicalOperator> extends AbstractRewriteRule<Operator> {

	@Override
	public int getPriority() {
		return 20;
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}
	
	protected boolean isValidProject(ProjectAO proj, Operator op) {
		if (proj.getInputAO() != null && proj.getInputAO().equals(op)) {
			return true;
		}
		return false;
	}
	
}