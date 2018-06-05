package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeshiftAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.TimeshiftPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTimeshiftAORule extends AbstractTransformationRule<TimeshiftAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(TimeshiftAO operator, TransformationConfiguration config) throws RuleException {
		PointInTime point = new PointInTime(operator.getShift());
		TimeshiftPO<ITimeInterval> po = new TimeshiftPO<>(point);
		defaultExecute(operator, po, config, true, false);		
	}

	@Override
	public boolean isExecutable(TimeshiftAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "TimeshiftAO -> TimeshiftPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
