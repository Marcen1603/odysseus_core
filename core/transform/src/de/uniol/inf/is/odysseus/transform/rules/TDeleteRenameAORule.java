package de.uniol.inf.is.odysseus.transform.rules;
import java.util.Collection;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDeleteRenameAORule extends AbstractTransformationRule<RenameAO>{

	@Override
	public int getPriority() {		
		return 1;
	}

	@Override
	public void execute(RenameAO rename, TransformationConfiguration transformConfig) {		
		LoggerFactory.getLogger("TDeleteRename - Rule").debug("RenameAO removing...: " + rename);
		Collection<ILogicalOperator> toUpdate = RestructHelper.removeOperator(rename, true);		
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		
		retract(rename);
		LoggerFactory.getLogger("TDeleteRename - Rule").debug("RenameAO removed: " + rename);
	}

	@Override
	public boolean isExecutable(RenameAO operator, TransformationConfiguration transformConfig) {	
		return !(operator.getInputAO() instanceof RenameAO);
	}

	@Override
	public String getName() {
		return "Delete Rename";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}
}
