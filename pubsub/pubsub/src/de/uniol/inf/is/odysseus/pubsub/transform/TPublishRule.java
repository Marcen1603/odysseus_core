package de.uniol.inf.is.odysseus.pubsub.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.pubsub.logicaloperator.Publish;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPublishRule extends AbstractTransformationRule<Publish> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(Publish publish, TransformationConfiguration config) {
		defaultExecute(publish, new PublishPO(publish.getDefaultBroker()), config,
				true, true);
	}

	@Override
	public boolean isExecutable(Publish operator,
			TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "Publish -> PublishPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super Publish> getConditionClass() {
		return Publish.class;
	}

}