package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.flow.RewriteRuleFlowGroup;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

public class RSwitchSelectionWindowRule extends AbstractRewriteRule<WindowAO> {

	@Override
	public int getPriority() {
		return 3;
	}

	@Override
	public void execute(WindowAO win, RewriteConfiguration config) {
		for(SelectAO sel : getAllOfSameTyp(new SelectAO())){
			if(isValidSelect(sel, win)){
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(sel,win);
				for (ILogicalOperator o:toUpdate){
					update(o);
				}

				update(win);
				update(sel);
			}
		}

	}

	@Override
	public boolean isExecutable(WindowAO win, RewriteConfiguration config) {		
		switch (win.getWindowType()) {
		case FIXED_TIME_WINDOW:
		case PERIODIC_TIME_WINDOW:
		case SLIDING_TIME_WINDOW:
		case JUMPING_TIME_WINDOW:
			return true;
		default:
			return false;
		}

	}

	@Override
	public String getName() {
		return "Switch Selection and Window";
	}
	
	private boolean isValidSelect(SelectAO sel, WindowAO win) {
		if (sel.getInputAO().equals(win)) {
			return true;
		}		
		return false;
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return RewriteRuleFlowGroup.SWITCH;
	}

}
