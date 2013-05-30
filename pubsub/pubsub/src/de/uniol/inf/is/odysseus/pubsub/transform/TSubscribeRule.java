package de.uniol.inf.is.odysseus.pubsub.transform;

import de.uniol.inf.is.odysseus.pubsub.logicaloperator.Subscribe;
import de.uniol.inf.is.odysseus.pubsub.physicaloperator.SubscribePO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TSubscribeRule extends AbstractTransformationRule<Subscribe> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(Subscribe subscribeAO, TransformationConfiguration config) {
		defaultExecute(subscribeAO, new SubscribePO(subscribeAO.getPredicates()), config,
				true, true);
	}

	@Override
	public boolean isExecutable(Subscribe operator,
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
	public Class<? super Subscribe> getConditionClass() {
		return Subscribe.class;
	}

}