package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.intervalapproach.AntiJoinTIPO;
import de.uniol.inf.is.odysseus.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDifferenceAORule extends AbstractTransformationRule<DifferenceAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DifferenceAO differenceAO, TransformationConfiguration transformConfig) {
		AntiJoinTIPO po = new AntiJoinTIPO(differenceAO);
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(differenceAO, po);
		for (ILogicalOperator o:toUpdate){
			update(o);
		}
		retract(differenceAO);
		
	}

	@Override
	public boolean isExecutable(DifferenceAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DifferenceAO -> AntiJoinTIPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
}
