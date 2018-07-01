package de.uniol.inf.is.odysseus.dsp.transform;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.logicaloperator.FIRFilterAO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFIRFilterAORule extends AbstractTransformationRule<FIRFilterAO> {

	@Override
	public void execute(FIRFilterAO operator, TransformationConfiguration config) throws RuleException {
		final TimeWindowAO timeWindowAO = new TimeWindowAO();
		timeWindowAO.setWindowSize(new TimeValueItem(1000, TimeUnit.MILLISECONDS));
		
		replace(operator, timeWindowAO, config);
		retract(operator);
		insert(timeWindowAO);
	}

	@Override
	public boolean isExecutable(FIRFilterAO operator, TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.SUBSTITUTION;
	}

}
