package de.uniol.inf.is.odysseus.relational.rewrite.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.SelectAO;
import de.uniol.inf.is.odysseus.relational.rewrite.RelationalRestructHelper;
import de.uniol.inf.is.odysseus.rewrite.engine.RewriteConfiguration;
import de.uniol.inf.is.odysseus.rewrite.rule.AbstractRewriteRule;

public class RSwitchSelectionRenameRule extends AbstractRewriteRule<RenameAO> {

	@Override
	public int getPriority() {	
		return 3;
	}

	@Override
	public void execute(RenameAO rename, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, rename)) {
				Collection<ILogicalOperator> toUpdate = RelationalRestructHelper.switchOperator(sel, rename);
				for (ILogicalOperator o:toUpdate){
					update(o);
				}					
				update(sel);
				update(rename);
			}
		}
		
	}

	@Override
	public boolean isExecutable(RenameAO ren, RewriteConfiguration config) {
		for (SelectAO sel : getAllOfSameTyp(new SelectAO())) {
			if (isValidSelect(sel, ren)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "Switch Selection and Rename";
	}
	
	private boolean isValidSelect(SelectAO sel, RenameAO ren) {
		if (sel.getInputAO().equals(ren)) {
			return true;
		}		
		return false;
	}

}
