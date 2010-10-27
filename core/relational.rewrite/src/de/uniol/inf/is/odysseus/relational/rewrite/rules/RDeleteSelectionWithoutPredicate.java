package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RDeleteSelectionWithoutPredicate extends AbstractRewriteRule<SelectAO> {

	@Override
	public int getPriority() {
		return 5;
	}

	@Override
	public void execute(SelectAO sel, RewriteConfiguration transformConfig) {
		// Wenn der Vorgänger nicht AggregateAO ist, dann		
		// Ausgabeelemente des Vorgängers auf die Ausgabeelemente des selects setzen.
		Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(sel);		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
	
		// Den SelectAO aus dem working memory löschen
		retract(sel);
		
	}

	@Override
	public boolean isExecutable(SelectAO sel, RewriteConfiguration transformConfig) {
		return (sel.getPredicate()==null);
	}

	@Override
	public String getName() {
		return "Delete Selection without Predicate";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.DELETE;
	}

}
