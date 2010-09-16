package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.logicaloperator.base.SplitAO;
import de.uniol.inf.is.odysseus.physicaloperator.base.SplitPO;
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
		SplitPO splitPO = new SplitPO(splitAO.getPredicates());
		splitPO.setOutputSchema(splitAO.getOutputSchema());
		Collection<ILogicalOperator> toUpdate = transformConfig.getTransformationHelper().replace(splitAO, splitPO);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
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
