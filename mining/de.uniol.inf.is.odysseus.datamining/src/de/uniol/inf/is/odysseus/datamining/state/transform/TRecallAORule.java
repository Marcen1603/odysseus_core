package de.uniol.inf.is.odysseus.datamining.state.transform;

import de.uniol.inf.is.odysseus.datamining.state.logicaloperator.RecallAO;
import de.uniol.inf.is.odysseus.datamining.state.physicaloperator.RecallPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRecallAORule extends AbstractTransformationRule<RecallAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(RecallAO operator, TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		RecallPO<?> po = new RecallPO(operator.getCheckList());
		po.setOutputSchema(operator.getOutputSchema());
		replace(operator, po, config);
		retract(operator);
		
	}

	@Override
	public boolean isExecutable(RecallAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "RecallAO -> RecallPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
