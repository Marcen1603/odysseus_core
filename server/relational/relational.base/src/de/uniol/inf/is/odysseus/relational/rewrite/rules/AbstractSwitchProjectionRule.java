package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public abstract class AbstractSwitchProjectionRule<Operator> extends
		AbstractRelationalRewriteRule<Operator> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}