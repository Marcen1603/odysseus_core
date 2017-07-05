package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.HeartbeatAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TInsertHeartbeatOperator extends AbstractTransformationRule<AbstractAccessAO> {

	@Override
	public void execute(AbstractAccessAO operator, TransformationConfiguration config) throws RuleException {
		for (LogicalSubscription subscription : operator.getSubscriptions()) {
			if (!(subscription.getTarget() instanceof HeartbeatAO)) {
				HeartbeatAO heartbeat = new HeartbeatAO();
				// TODO: determine heartbeat options from access
				heartbeat.setRealTimeDelay(1000);
				heartbeat.setApplicationTimeDelay(1000);
				heartbeat.setSendAlwaysHeartbeat(true);
				heartbeat.setAllowOutOfOrder(false);
				heartbeat.setRestartTimerForEveryInput(true);
				
				Collection<ILogicalOperator> toUpdate = RestructHelper.insertOperatorBefore(heartbeat, operator,
						subscription.getSinkInPort(), subscription.getSourceOutPort(),0);

				insert(heartbeat);

				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
			}
		}
	}

	@Override
	public boolean isExecutable(AbstractAccessAO operator, TransformationConfiguration config) {
		if (!operator.getOutputSchema().isInOrder()) {

			for (LogicalSubscription subscription : operator.getSubscriptions()) {
				if (!(subscription.getTarget() instanceof HeartbeatAO)) {
					return true;
				}
			}

			return false;
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.OUTOFORDER;
	}

}
