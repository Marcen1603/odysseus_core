package de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.LoadBalancingTestReceiverAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.LoadBalancingTestReceiverPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class LoadBalancingTestReceiverAORule extends
		AbstractTransformationRule<LoadBalancingTestReceiverAO> {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(LoadBalancingTestReceiverAO operator,
			TransformationConfiguration config) {
		defaultExecute(operator, new LoadBalancingTestReceiverPO(operator),
				config, true, true);
	}

	@Override
	public boolean isExecutable(LoadBalancingTestReceiverAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "LoadBalancingTestReceiverAO --> LoadBalancingTestReceiverPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
