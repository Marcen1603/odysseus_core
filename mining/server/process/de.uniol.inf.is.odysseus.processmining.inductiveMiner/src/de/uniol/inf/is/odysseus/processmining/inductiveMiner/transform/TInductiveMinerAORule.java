package de.uniol.inf.is.odysseus.processmining.inductiveMiner.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.logicaloperator.InductiveMinerAO;
import de.uniol.inf.is.odysseus.processmining.inductiveMiner.physicaloperator.InductiveMinerPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TInductiveMinerAORule extends AbstractTransformationRule<InductiveMinerAO>{
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(InductiveMinerAO logicalOp, TransformationConfiguration transformConfig)
			throws RuleException {
		defaultExecute(logicalOp, new InductiveMinerPO(logicalOp.getInvariantType(),logicalOp.getGeneratingStrategy()), transformConfig, true, true);
		
	}

	@Override
	public boolean isExecutable(InductiveMinerAO logicalOp,	TransformationConfiguration config) {
		return logicalOp.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName(){
		return "TInductiveMinerAO -> TInductiveMinerPO";
	}

}
