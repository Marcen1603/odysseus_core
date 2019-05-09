package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.HeartbeatAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SessionWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSessionWindowAORule extends AbstractTransformationRule<SessionWindowAO> {

	@Override
	public void execute(SessionWindowAO sessionWindow, TransformationConfiguration config) throws RuleException {
		// replace session window with heartbeat and predicate window

		// configuration of heartbeat:
		// heartbeat is only sent if it does not receive any new element within the
		// given timespan.
		PredicateWindowAO predicateWindow = new PredicateWindowAO();
		predicateWindow
				.setStartCondition(new RelationalExpression<>(new SDFExpression("true", null, MEP.getInstance())));
		predicateWindow
				.setEndCondition(new RelationalExpression<>(new SDFExpression("false", null, MEP.getInstance())));
		predicateWindow.setCloseWindowWithHeartbeat(true);
		LogicalPlan.replace(sessionWindow, predicateWindow);
		insert(predicateWindow);

		// configuation of predicate window:
		// start condition holds always and end never. closing the window is only bone
		// by heartbeat
		HeartbeatAO heartbeat = new HeartbeatAO();
		heartbeat.setApplicationTimeDelay(sessionWindow.getThreshold());
		heartbeat.setRealTimeDelay(sessionWindow.getThreshold());
		heartbeat.setSendAlwaysHeartbeat(false);
		heartbeat.setAllowOutOfOrder(false);
		heartbeat.setStartAtCurrentTime(sessionWindow.startAtCurrentTime());
		heartbeat.setSendOnlyOneHeartbeat(true);
		LogicalPlan.insertOperator(heartbeat, predicateWindow, 0, 0, 0);
		insert(heartbeat);

		retract(sessionWindow);
	}

	@Override
	public boolean isExecutable(SessionWindowAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

	@Override
	public Class<? super SessionWindowAO> getConditionClass() {
		return SessionWindowAO.class;
	}

}
