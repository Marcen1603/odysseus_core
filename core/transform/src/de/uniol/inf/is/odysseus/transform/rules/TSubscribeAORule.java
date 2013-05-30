package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.publishsubscribe.logicaloperator.SubscribeAO;
import de.uniol.inf.is.odysseus.core.server.publishsubscribe.physicaloperator.SubscribePO;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class TSubscribeAORule extends AbstractTransformationRule<SubscribeAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(SubscribeAO subscribeAO, TransformationConfiguration config) {
		defaultExecute(subscribeAO, new SubscribePO(subscribeAO.getPredicates()), config,
				true, true);
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