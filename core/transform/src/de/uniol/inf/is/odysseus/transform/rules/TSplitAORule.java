package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.logicaloperator.SplitAO;
import de.uniol.inf.is.odysseus.physicaloperator.SplitPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSplitAORule extends AbstractTransformationRule<SplitAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	
	@Override
	public void execute(SplitAO splitAO, TransformationConfiguration transformConfig) {
		@SuppressWarnings({ "unchecked", "rawtypes" })
		SplitPO splitPO = new SplitPO(splitAO.getPredicates());
		splitPO.setOutputSchema(splitAO.getOutputSchema());
		replace(splitAO, splitPO, transformConfig);
		retract(splitAO);

	}

	@Override
	public boolean isExecutable(SplitAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SplitAO -> SplitPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
