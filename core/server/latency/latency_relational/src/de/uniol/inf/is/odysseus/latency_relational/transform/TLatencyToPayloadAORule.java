package de.uniol.inf.is.odysseus.latency_relational.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.latency_relational.LatencyToPayloadPO;
import de.uniol.inf.is.odysseus.logicaloperator.latency.LatencyToPayloadAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyToPayloadAORule extends AbstractTransformationRule<LatencyToPayloadAO>{

	@Override
	public void execute(LatencyToPayloadAO operator, TransformationConfiguration config) throws RuleException {
		LatencyToPayloadPO<ILatency, Tuple<ILatency>> po = new LatencyToPayloadPO<>(operator);
		defaultExecute(operator, po, config, true, false);	
	}

	@Override
	public boolean isExecutable(LatencyToPayloadAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema(0).getType() == Tuple.class
				&& operator.getInputSchema(0).hasMetatype(
						ILatency.class)) {
			if (operator.isAllPhysicalInputSet()) {
				return true;
			}
		}
		return false;	}


	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
