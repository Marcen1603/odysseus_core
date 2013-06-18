package de.uniol.inf.is.odysseus.pubsub.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.pubsub.logicaloperator.PublishAO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.PublishPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPublishAORule extends AbstractTransformationRule<PublishAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(PublishAO publish, TransformationConfiguration config) {
		defaultExecute(publish, new PublishPO(publish.getTopologyType(),
				publish.getDomain(), publish.getTopics()), config, true, true);
	}

	@Override
	public boolean isExecutable(PublishAO operator,
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
	public Class<? super PublishAO> getConditionClass() {
		return PublishAO.class;
	}

}