package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.ChangeDetectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.RelationalChangeDetectPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TRelationalChangeDetectAORule extends
		AbstractTransformationRule<ChangeDetectAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(ChangeDetectAO operator,
			TransformationConfiguration config) {
		RelationalChangeDetectPO po = new RelationalChangeDetectPO(operator.getComparePositions());
		replace(operator, po, config);		
		po.setOutputSchema(operator.getOutputSchema());
		retract(operator);
	}

	@Override
	public boolean isExecutable(ChangeDetectAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet() && (operator.getAttributes() != null && operator.getAttributes().size() > 0);
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
