package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.optimizer.ExpressionOptimizer;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class ROptimizeSelectionPredicate extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration config)
			throws RuleException {
		SDFExpression originalSDFExpression = ((RelationalPredicate) sel
				.getPredicate()).getExpression();
		IExpression<?> optimizedExpression = ExpressionOptimizer.optimize(originalSDFExpression.getMEPExpression());
		if(!originalSDFExpression.getMEPExpression().equals(optimizedExpression)) {
			sel.setPredicate(new RelationalPredicate(new SDFExpression(
					optimizedExpression, originalSDFExpression.getAttributeResolver(),
					originalSDFExpression.getExpressionParser())));
			RestructParameterInfoUtil.updatePredicateParameterInfo(
					sel.getParameterInfos(), sel.getPredicate());
			update(sel);
		}
	}

	@Override
	public boolean isExecutable(SelectAO sel, RewriteConfiguration config) {
		return sel != null && sel.getPredicate() != null
				&& RelationalPredicate.class.isInstance(sel.getPredicate());
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.CLEANUP;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		return SelectAO.class;
	}

}