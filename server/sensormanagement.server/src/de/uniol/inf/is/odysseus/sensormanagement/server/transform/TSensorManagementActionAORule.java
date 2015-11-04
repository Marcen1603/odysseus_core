package de.uniol.inf.is.odysseus.sensormanagement.server.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sensormanagement.server.logicaloperator.SensorManagementActionAO;
import de.uniol.inf.is.odysseus.sensormanagement.server.physicaloperator.SensorManagementActionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSensorManagementActionAORule extends AbstractTransformationRule<SensorManagementActionAO> {

	@Override
	public void execute(SensorManagementActionAO operator, TransformationConfiguration config) throws RuleException {
		SensorManagementActionPO po = new SensorManagementActionPO(operator); 
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(SensorManagementActionAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SensorManagementActionAO> getConditionClass() {
		return SensorManagementActionAO.class;
	}
}
