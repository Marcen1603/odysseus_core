package de.uniol.inf.is.odysseus.server.intervalapproach.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.server.intervalapproach.SyncWithSystemTimePO;
import de.uniol.inf.is.odysseus.server.intervalapproach.logicaloperator.SyncWithSystemTimeAO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TSyncWithSystemTimeAORule extends AbstractIntervalTransformationRule<SyncWithSystemTimeAO> {

	@Override
	public void execute(SyncWithSystemTimeAO operator,
			TransformationConfiguration config) throws RuleException {
		SyncWithSystemTimePO<IStreamObject<? extends ITimeInterval>> po = new SyncWithSystemTimePO<>(operator.getTimeUnit(), operator.getFactor());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SyncWithSystemTimeAO> getConditionClass() {
		return SyncWithSystemTimeAO.class;
	}
	
}
