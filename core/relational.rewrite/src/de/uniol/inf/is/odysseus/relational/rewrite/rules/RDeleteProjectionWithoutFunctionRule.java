package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RDeleteProjectionWithoutFunctionRule extends AbstractRewriteRule<ProjectAO> {

	@Override
	public int getPriority() {		
		return 5;
	}

	@Override
	public void execute(ProjectAO proj, RewriteConfiguration transformConfig) {
		Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.removeOperator(proj);		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}		 
		// Den ProjectAO aus dem working memory löschen
		retract(proj);
		
	}

	@Override
	public boolean isExecutable(ProjectAO proj, RewriteConfiguration transformConfig) {
		return proj.getInputSchema().equals(proj.getOutputSchema());
	}

	@Override
	public String getName() {
		return "Delete Projection without function";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.DELETE;
	}

}
