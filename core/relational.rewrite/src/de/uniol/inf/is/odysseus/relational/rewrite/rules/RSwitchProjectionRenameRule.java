package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.planmanagement.optimization.configuration.RewriteConfiguration;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchProjectionRenameRule extends AbstractRewriteRule<RenameAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(RenameAO rename, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(rename)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(proj, rename);
				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
				update(rename);
				update(proj);
			}
		}
	}

	@Override
	public boolean isExecutable(RenameAO rename, RewriteConfiguration config) {
		for (ProjectAO proj : this.getAllOfSameTyp(new ProjectAO())) {
			if (proj.getInputAO().equals(rename)) {
				return true;
			}
		}
		return false;
		
	}

	@Override
	public String getName() {
		return "Switch Projection and Rename";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}
