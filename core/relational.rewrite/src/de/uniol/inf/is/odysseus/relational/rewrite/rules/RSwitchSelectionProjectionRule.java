package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchSelectionProjectionRule extends AbstractRewriteRule<ProjectAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(ProjectAO proj, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, proj)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(sel, proj);
				for (ILogicalOperator o:toUpdate){
					update(o);
				}

				update(proj);
				update(sel);
			}
		}

	}


	@Override
	public boolean isExecutable(ProjectAO proj, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, proj)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Switch Selection and Projection";
	}
	

	private boolean isValidSelect(SelectAO sel, ProjectAO proj) {
		if (sel.getInputAO().equals(proj)) {
			return true;
		}		
		return false;
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}
