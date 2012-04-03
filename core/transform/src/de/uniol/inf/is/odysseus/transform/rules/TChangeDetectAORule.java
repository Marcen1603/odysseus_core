package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UserDefinedOperatorAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TChangeDetectAORule extends
		AbstractTransformationRule<ChangeDetectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ChangeDetectAO operator,
			TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		ChangeDetectPO<?> po = new ChangeDetectPO();
		replace(operator, po, config);		
		retract(operator);
	}

	@Override
	public boolean isExecutable(ChangeDetectAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "ChangeDetectAO -> ChangeDetectPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {
		return ChangeDetectAO.class;
	}

}
