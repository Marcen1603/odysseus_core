package de.uniol.inf.is.odysseus.pubsub.transform;

import de.uniol.inf.is.odysseus.pubsub.logicaloperator.SubscribeAO;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

public class TSubscribeAORule extends AbstractTransformationRule<SubscribeAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(SubscribeAO subscribe,
			TransformationConfiguration config) {
		defaultExecute(
				subscribe,
				new SubscribePO(subscribe.getPredicates(), subscribe
						.getBrokerName(), subscribe.getSchema(), subscribe
						.getTopics(), subscribe.getDomain()), config, true,
				true);
	}

	@Override
	public boolean isExecutable(SubscribeAO operator,
			TransformationConfiguration transformConfig) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SubscribeAO -> SubscribePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SubscribeAO> getConditionClass() {
		return SubscribeAO.class;
	}

}