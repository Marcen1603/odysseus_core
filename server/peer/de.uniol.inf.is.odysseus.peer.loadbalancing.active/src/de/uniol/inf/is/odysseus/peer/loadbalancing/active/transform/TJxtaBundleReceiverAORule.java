package de.uniol.inf.is.odysseus.peer.loadbalancing.active.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.data.DataTransmissionException;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.logicaloperator.JxtaBundleReceiverAO;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.physicaloperator.JxtaBundleReceiverPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TJxtaBundleReceiverAORule extends
		AbstractTransformationRule<JxtaBundleReceiverAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(JxtaBundleReceiverAO operator,
			TransformationConfiguration config) throws RuleException {
		try {
			defaultExecute(operator, new JxtaBundleReceiverPO(operator),
					config, true, true);

		} catch (DataTransmissionException e) {
			throw new RuleException("Could not create JxtaBundleSenderPO", e);
		}
	}

	@Override
	public boolean isExecutable(JxtaBundleReceiverAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "JxtaBundleReceiverAO --> JxtaBundleReceiverPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
