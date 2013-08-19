package de.uniol.inf.is.odysseus.interval.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.SyncWithSystemTimePO;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.SyncWithSystemTimeAO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSyncWithSystemTimeAORule extends AbstractTransformationRule<SyncWithSystemTimeAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SyncWithSystemTimeAO operator,
			TransformationConfiguration config) {
		SyncWithSystemTimePO<IStreamObject<? extends ITimeInterval>, IStreamObject<? extends ITimeInterval>> po = new SyncWithSystemTimePO<>(operator.getTimeUnit());
		defaultExecute(operator, po, config, true, true);
	}

	@Override
	public boolean isExecutable(SyncWithSystemTimeAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SyncWithSystemTimeAO --> SyncWithSystemTimePO";
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
