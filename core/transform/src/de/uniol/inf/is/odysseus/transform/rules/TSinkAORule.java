package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SinkAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SinkPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSinkAORule extends AbstractTransformationRule<SinkAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SinkAO operator,
			TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		SinkPO sinkPO = new SinkPO();
		replace(operator, sinkPO, config);
		retract(operator);
	}

	@Override
	public boolean isExecutable(SinkAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SinkAO -> SinkPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<?> getConditionClass() {
		return SinkAO.class;
	}

	
}
