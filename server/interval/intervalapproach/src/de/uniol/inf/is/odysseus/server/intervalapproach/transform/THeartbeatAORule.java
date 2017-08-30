package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.HeartbeatAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.HeartbeatPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class THeartbeatAORule extends
		AbstractIntervalTransformationRule<HeartbeatAO> {

	@Override
	public void execute(HeartbeatAO operator,
			TransformationConfiguration config) throws RuleException {
		HeartbeatPO<IStreamObject<? extends ITimeInterval>> physical = new HeartbeatPO<>(operator.isStartAtCurrentTime());
		physical.setApplicationTimeDelay(operator.getApplicationTimeDelay());
		physical.setRealTimeDelay(operator.getRealTimeDelay(), operator.getTimeUnit());
		physical.setSendAlwaysHeartbeat(operator.isSendAlwaysHeartbeat());
		physical.setStartTimerAfterFirstElement(operator.isStartTimerAfterFirstElement());
		physical.setRestartTimerForEveryInput(operator.isRestartTimerForEveryInput());
		defaultExecute(operator, physical, config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super HeartbeatAO> getConditionClass() {
		return HeartbeatAO.class;
	}

}
