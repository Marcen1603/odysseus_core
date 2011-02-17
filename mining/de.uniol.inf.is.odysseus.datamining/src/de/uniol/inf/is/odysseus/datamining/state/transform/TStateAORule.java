package de.uniol.inf.is.odysseus.datamining.state.transform;

import de.uniol.inf.is.odysseus.datamining.state.logicaloperator.StateAO;
import de.uniol.inf.is.odysseus.datamining.state.physicaloperator.StatePO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TStateAORule extends AbstractTransformationRule<StateAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(StateAO operator, TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		StatePO<?> po = new StatePO();
		po.setOpen(operator.isOpen());
		po.setOpenAt(operator.getOpenAt());
		replace(operator, po, config);	
		retract(operator);
	}

	@Override
	public boolean isExecutable(StateAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "StateAO -> StatePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
