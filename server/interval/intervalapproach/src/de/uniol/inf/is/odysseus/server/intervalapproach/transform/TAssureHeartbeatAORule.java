package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.AssureHeartbeatPO;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.AssureHeartbeatAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TAssureHeartbeatAORule extends
		AbstractIntervalTransformationRule<AssureHeartbeatAO> {

	@Override
	public void execute(AssureHeartbeatAO operator,
			TransformationConfiguration config) throws RuleException {
		AssureHeartbeatPO<IStreamObject<? extends ITimeInterval>> physical = new AssureHeartbeatPO<>(operator.isStartAtCurrentTime());
		physical.setApplicationTimeDelay(operator.getApplicationTimeDelay());
		physical.setRealTimeDelay(operator.getRealTimeDelay(), operator.getTimeUnit());
		physical.setSendAlwaysHeartbeat(operator.isSendAlwaysHeartbeat());
		physical.setAllowOutOfOrder(operator.isAllowOutOfOrder());
		physical.setStartTimerAfterFirstElement(operator.isStartTimerAfterFirstElement());
		defaultExecute(operator, physical, config, true, true);
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
