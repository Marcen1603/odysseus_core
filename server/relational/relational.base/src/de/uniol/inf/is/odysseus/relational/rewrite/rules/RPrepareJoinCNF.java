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

public class RPrepareJoinCNF extends AbstractRewriteRule<JoinAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(JoinAO join, RewriteConfiguration config)
			throws RuleException {
		SDFExpression originalSDFExpression = ((RelationalPredicate) join
				.getPredicate()).getExpression();
		IExpression<?> expressionInCNF = ExpressionOptimizer
				.toConjunctiveNormalForm(originalSDFExpression
						.getMEPExpression());
		if(!originalSDFExpression.getMEPExpression().equals(expressionInCNF)) {
			join.setPredicate(new RelationalPredicate(new SDFExpression(
					expressionInCNF, originalSDFExpression.getAttributeResolver(),
					originalSDFExpression.getExpressionParser())));
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
		return RewriteRuleFlowGroup.PREPARE;
	}

	@Override
	public Class<? super JoinAO> getConditionClass() {
		return JoinAO.class;
	}

}