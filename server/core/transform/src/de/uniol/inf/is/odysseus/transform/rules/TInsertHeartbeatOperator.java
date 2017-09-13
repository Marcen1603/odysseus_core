package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.HeartbeatAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * In cases where the output of an access operator is out of order, a heartbeat operator needs to
 * be inserted to assure, that elements get processed
 * 
 * @author Marco Grawunder
 *
 */
public class TInsertHeartbeatOperator extends AbstractTransformationRule<AbstractAccessAO> {

	@Override
	public void execute(AbstractAccessAO operator, TransformationConfiguration config) throws RuleException {
		for (LogicalSubscription subscription : operator.getSubscriptions()) {
			if (!(subscription.getSink() instanceof HeartbeatAO)) {
				HeartbeatAO heartbeat = new HeartbeatAO();
				long realTimeDelay = operator.getRealTimeDelay();
				long applicationTimeDelay = operator.getApplicationTimeDelay();
				
				heartbeat.setRealTimeDelay(realTimeDelay);
				heartbeat.setApplicationTimeDelay(applicationTimeDelay);
				heartbeat.setSendAlwaysHeartbeat(true);
				heartbeat.setAllowOutOfOrder(true);
				heartbeat.setRestartTimerForEveryInput(true);
				
				Collection<ILogicalOperator> toUpdate = LogicalPlan.insertOperatorBefore(heartbeat, operator,
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
				if (!(subscription.getSink() instanceof HeartbeatAO)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.OUTOFORDER_INIT;
	}

}
