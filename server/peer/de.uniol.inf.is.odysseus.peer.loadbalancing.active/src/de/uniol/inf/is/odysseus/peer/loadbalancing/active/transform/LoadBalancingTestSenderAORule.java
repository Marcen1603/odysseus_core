package de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.LoadBalancingTestSenderAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingTestSenderPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class LoadBalancingTestSenderAORule extends
		AbstractTransformationRule<LoadBalancingTestSenderAO> {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(LoadBalancingTestSenderAO operator,
			TransformationConfiguration config) {
		defaultExecute(operator, new LoadBalancingTestSenderPO(operator),
				config, true, true);
	}

	@Override
	public boolean isExecutable(LoadBalancingTestSenderAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "LoadBalancingTestSenderAO --> LoadBalancingTestSenderPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
