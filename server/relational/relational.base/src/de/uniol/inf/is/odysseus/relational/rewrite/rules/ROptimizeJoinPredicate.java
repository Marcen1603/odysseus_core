package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.optimizer.ExpressionOptimizer;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class ROptimizeJoinPredicate extends AbstractRewriteRule<JoinAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinAO join, RewriteConfiguration config)
			throws RuleException {
		SDFExpression originalSDFExpression = ((RelationalPredicate) join
				.getPredicate()).getExpression();
		IExpression<?> optimizedExpression = ExpressionOptimizer
				.optimize(originalSDFExpression.getMEPExpression());
		if(!originalSDFExpression.getMEPExpression().equals(optimizedExpression)) {
			join.setPredicate(new RelationalPredicate(new SDFExpression(
					optimizedExpression, originalSDFExpression
							.getAttributeResolver(), originalSDFExpression
							.getExpressionParser())));
			RestructParameterInfoUtil.updatePredicateParameterInfo(
					join.getParameterInfos(), join.getPredicate());
			update(join);
		}
	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		return join != null && join.getPredicate() != null
				&& RelationalPredicate.class.isInstance(join.getPredicate());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.CLEANUP;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

}