package de.uniol.inf.is.odysseus.dsp.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.sample.logicaloperator.SampleSignalAO;
import de.uniol.inf.is.odysseus.dsp.sample.physicaloperator.ConstantInterpolation;
import de.uniol.inf.is.odysseus.dsp.sample.physicaloperator.InterpolationMethod;
import de.uniol.inf.is.odysseus.dsp.sample.physicaloperator.SampleSignalPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSampleSignalAORule extends AbstractTransformationRule<SampleSignalAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SampleSignalAO sampleSignalAO, TransformationConfiguration config) throws RuleException {

		final InterpolationMethod interpolationMethod;
		switch (sampleSignalAO.getInterpolationPolicy()) {
		case CONSTANT:
			interpolationMethod = new ConstantInterpolation();
			break;
		case LINEAR:
			// todo
			interpolationMethod = new ConstantInterpolation();
			break;
		default:
			interpolationMethod = new ConstantInterpolation();
			break;
		}
		defaultExecute(sampleSignalAO, new SampleSignalPO(sampleSignalAO.getSampleInterval(), interpolationMethod),
				config, true, true);
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
