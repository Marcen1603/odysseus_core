package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.predicate.AndPredicate;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RMergeSelectionRule extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SelectAO operator, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (sel.getInputAO()== operator) {
				if (sel.getPredicate() != null) {
					if (operator.getPredicate() != null) {
						operator.setPredicate(new AndPredicate(operator.getPredicate(), sel.getPredicate()));
					} else {
						operator.setPredicate(sel.getPredicate());
					}
					Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(sel);
					for (ILogicalOperator o : toUpdate) {
						update(o);
					}
					update(operator);
					retract(sel);
				}
			}
		}		
	}

	
	@Override
	public boolean isExecutable(SelectAO operator, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (sel.getInputAO()== operator) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Merge Selection";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}
