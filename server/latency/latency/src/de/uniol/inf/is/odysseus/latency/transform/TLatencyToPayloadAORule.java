package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency.physicaloperator.LatencyToPayloadPO;
import de.uniol.inf.is.odysseus.logicaloperator.latency.LatencyToPayloadAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyToPayloadAORule extends AbstractTransformationRule<LatencyToPayloadAO>{

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(LatencyToPayloadAO operator, TransformationConfiguration config) throws RuleException {
		LatencyToPayloadPO<ILatency, IStreamObject<ILatency>> po = new LatencyToPayloadPO<>();
		defaultExecute(operator, po, config, true, false);	
	}

	@Override
	public boolean isExecutable(LatencyToPayloadAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "LatencyToPayloadAO -> LatencyToPayloadPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
