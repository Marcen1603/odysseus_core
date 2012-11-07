package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.AssureHeartbeatPO;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAssureHeartbeatAORule extends
		AbstractTransformationRule<AssureHeartbeatAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AssureHeartbeatAO operator,
			TransformationConfiguration config) {
		AssureHeartbeatPO<IStreamObject<? extends ITimeInterval>> physical = new AssureHeartbeatPO<>();
		physical.setApplicationTimeDelay(operator.getApplicationTimeDelay());
		physical.setRealTimeDelay(operator.getRealTimeDelay(), operator.getTimeUnit());
		physical.setSendAlwaysHeartbeat(operator.isSendAlwaysHeartbeat());
		physical.setAllowOutOfOrder(operator.isAllowOutOfOrder());
		defaultExecute(operator, physical, config, true, true);
	}

	@Override
	public boolean isExecutable(AssureHeartbeatAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "AssureHeartbeatAO --> AssureHeartbeatPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AssureHeartbeatAO> getConditionClass() {
		return AssureHeartbeatAO.class;
	}

}
