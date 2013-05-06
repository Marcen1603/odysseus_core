package de.uniol.inf.is.odysseus.mining.transform;

import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mining.logicaloperator.LatencyConverterAO;
import de.uniol.inf.is.odysseus.mining.physicaloperator.LatencyConverterPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyConverterAORule extends AbstractTransformationRule<LatencyConverterAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(LatencyConverterAO operator, TransformationConfiguration config) {
		defaultExecute(operator, new LatencyConverterPO(operator.getFactor(), operator.getSample()), config, true, true);
	}

	@Override
	public boolean isExecutable(LatencyConverterAO operator, TransformationConfiguration config) {
		if (operator.isAllPhysicalInputSet()) {
			if (config.getMetaTypes().contains(ILatency.class.getCanonicalName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getName() {
		return "LatencyConverterAO -> LatencyConverterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super LatencyConverterAO> getConditionClass() {	
		return LatencyConverterAO.class;
	}

}
