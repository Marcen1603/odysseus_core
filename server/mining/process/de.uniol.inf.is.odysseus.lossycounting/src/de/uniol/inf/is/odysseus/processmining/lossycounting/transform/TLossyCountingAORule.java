package de.uniol.inf.is.odysseus.processmining.lossycounting.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.processmining.lossycounting.logicaloperator.LossyCountingAO;
import de.uniol.inf.is.odysseus.processmining.lossycounting.physicaloperator.LossyCountingPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLossyCountingAORule extends AbstractTransformationRule<LossyCountingAO>{
	
	@SuppressWarnings("rawtypes")
	@Override
	public void execute(LossyCountingAO logicalOp, TransformationConfiguration transformConfig)
			throws RuleException {
		defaultExecute(logicalOp, new LossyCountingPO(logicalOp.getBucketWidth(),logicalOp.getMinFrequence()),
				transformConfig, true, true);
	}

	@Override
	public boolean isExecutable(LossyCountingAO logicalOp,
			TransformationConfiguration config) {
		return logicalOp.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName(){
		return "LossyCounting for Process Mining";
	}
}
