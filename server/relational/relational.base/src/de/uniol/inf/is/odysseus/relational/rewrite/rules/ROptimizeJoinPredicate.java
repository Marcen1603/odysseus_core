package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.expression.AbstractRelationalExpression;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;
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
	public void execute(JoinAO join, RewriteConfiguration config) throws RuleException {
		AbstractRelationalExpression<?> originalSDFExpression = ((AbstractRelationalExpression<?>) join.getPredicate());
		IMepExpression<?> optimizedExpression = BooleanExpressionOptimizer
				.optimize(originalSDFExpression.getMEPExpression());
		if (!originalSDFExpression.getMEPExpression().equals(optimizedExpression)) {
			RelationalExpression<?> predicate = new RelationalExpression<>(
					new SDFExpression(optimizedExpression.toString(), originalSDFExpression.getAttributeResolver(),
							originalSDFExpression.getExpressionParser()));
			join.setPredicate(predicate);
			predicate.initVars(join.getInputSchema(0), join.getInputSchema(1));

			RestructParameterInfoUtil.updatePredicateParameterInfo(join, join.getParameterInfos(), join.getPredicate());
			update(join);
		}
	}

	@Override
	public boolean isExecutable(JoinAO join, RewriteConfiguration config) {
		return join != null && join.getPredicate() != null
				&& RelationalExpression.class.isInstance(join.getPredicate());
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