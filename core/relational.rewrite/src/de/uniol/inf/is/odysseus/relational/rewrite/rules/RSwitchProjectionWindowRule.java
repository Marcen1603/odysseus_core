package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchProjectionWindowRule extends AbstractRewriteRule<WindowAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(WindowAO win, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(win)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(proj, win);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(win);
				update(proj);
			}
		}
	}

	@Override
	public boolean isExecutable(WindowAO win, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(win)) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String getName() {
		return "Switch Projection and Window";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}
