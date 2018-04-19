package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.SampleAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.ElementSamplePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.TimeSamplePO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSampleAORule extends AbstractTransformationRule<SampleAO> {

	@Override
	public void execute(SampleAO operator, TransformationConfiguration config) throws RuleException {
		if (operator.getTimeValue() != null) {
			defaultExecute(operator, new TimeSamplePO<>(operator), config, true, true);
		} else {
			defaultExecute(operator, new ElementSamplePO<>(operator), config, true, true);
		}
	}

	@Override
	public boolean isExecutable(SampleAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}
	
	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super SampleAO> getConditionClass() {
		return SampleAO.class;
	}
}
