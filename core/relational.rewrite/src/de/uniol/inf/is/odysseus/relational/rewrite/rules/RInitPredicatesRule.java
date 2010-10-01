package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.relational.rewrite.InitPredicateFunctor;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RInitPredicatesRule extends AbstractRewriteRule<ILogicalOperator> {

	@Override
	public int getPriority() {
		return -1;
	}

	@Override
	public void execute(ILogicalOperator operator, RewriteConfiguration config) {
		operator.getPredicate().init();
		RelationalRestructHelper.visitPredicates(operator.getPredicate(), new InitPredicateFunctor(operator));
	}

	@Override
	public boolean isExecutable(ILogicalOperator operator, RewriteConfiguration config) {
		return (operator.getPredicate()!=null);
	}

	@Override
	public String getName() {
		return "Init Predicates";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.CLEANUP;
	}

}
