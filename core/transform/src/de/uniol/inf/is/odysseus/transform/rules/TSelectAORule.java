package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSelectAORule extends AbstractTransformationRule<SelectAO> {

	@Override
	public int getPriority() {		
		return 0;
	}

	@Override
	public void execute(SelectAO selectAO, TransformationConfiguration transformConfig) {		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
		selectPO.setOutputSchema(selectAO.getOutputSchema());
		replace(selectAO, selectPO, transformConfig);		
		retract(selectAO);
		
	}

	@Override
	public boolean isExecutable(SelectAO operator, TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SelectAO -> SelectPO";
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
