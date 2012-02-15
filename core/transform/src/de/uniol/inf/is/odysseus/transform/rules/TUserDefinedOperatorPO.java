package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.logicaloperator.UserDefinedOperatorAO;
import de.uniol.inf.is.odysseus.physicaloperator.UserDefinedOperatorPO;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUserDefinedOperatorPO extends
		AbstractTransformationRule<UserDefinedOperatorAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(UserDefinedOperatorAO operator,
			TransformationConfiguration config) {
		UserDefinedOperatorPO po = new UserDefinedOperatorPO();
		po.setOutputSchema(operator.getOutputSchema());
		po.setInitString(operator.getInitString());
		po.setUdf(operator.getUdf());		
		replace(operator, po, config);		
		retract(operator);
	}

	@Override
	public boolean isExecutable(UserDefinedOperatorAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "UdoAO -> UdoPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {
		return UserDefinedOperatorAO.class;
	}

}
