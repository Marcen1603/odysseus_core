package de.uniol.inf.is.odysseus.dsp.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.sample.logicaloperator.SampleSignalAO;
import de.uniol.inf.is.odysseus.dsp.sample.physicaloperator.SampleSignalPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSampleSignalAORule extends AbstractTransformationRule<SampleSignalAO> {

	@Override
	public void execute(SampleSignalAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new SampleSignalPO(), config, true, true);
	}

	@Override
	public boolean isExecutable(SampleSignalAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
