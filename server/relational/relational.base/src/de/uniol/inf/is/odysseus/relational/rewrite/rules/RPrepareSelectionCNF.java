package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RPrepareSelectionCNF extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration config)
			throws RuleException {
		SDFExpression originalSDFExpression = ((RelationalPredicate) sel
				.getPredicate()).getExpression();
		IExpression<?> expressionInCNF = BooleanExpressionOptimizer
				.toConjunctiveNormalForm(BooleanExpressionOptimizer.optimize(originalSDFExpression
						.getMEPExpression()));
		if(!originalSDFExpression.getMEPExpression().equals(expressionInCNF)) {
			RelationalPredicate predicate = new RelationalPredicate(new SDFExpression(
					expressionInCNF, originalSDFExpression.getAttributeResolver(),
					originalSDFExpression.getExpressionParser()));
			sel.setPredicate(predicate);
			predicate.init(sel.getInputSchema(), null, false);	
			
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
		return RewriteRuleFlowGroup.PREPARE;
	}

	@Override
	public Class<? super SelectAO> getConditionClass() {
		return SelectAO.class;
	}

}