package de.uniol.inf.is.odysseus.latency.transform;

import de.uniol.inf.is.odysseus.latency.LatencyCalculationAO;
import de.uniol.inf.is.odysseus.latency.LatencyCalculationPipe;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TLatencyCalculationRule extends
		AbstractTransformationRule<LatencyCalculationAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(LatencyCalculationAO operator,
			TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		LatencyCalculationPipe pO = new LatencyCalculationPipe();
		pO.setOutputSchema(operator.getOutputSchema());
		replace(operator, pO, config);		
		retract(operator);		
	}

	@Override
	public boolean isExecutable(LatencyCalculationAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "LatencyCalculationAO -> LatencyCalculationPipe";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
