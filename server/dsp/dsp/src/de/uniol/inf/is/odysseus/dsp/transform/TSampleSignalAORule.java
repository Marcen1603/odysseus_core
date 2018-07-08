package de.uniol.inf.is.odysseus.dsp.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.dsp.interpolation.ConstantInterpolation;
import de.uniol.inf.is.odysseus.dsp.interpolation.IInterpolationMethod;
import de.uniol.inf.is.odysseus.dsp.interpolation.LinearInterpolation;
import de.uniol.inf.is.odysseus.dsp.logicaloperator.SampleSignalAO;
import de.uniol.inf.is.odysseus.dsp.physicaloperator.SampleSignalPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSampleSignalAORule extends AbstractTransformationRule<SampleSignalAO> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(SampleSignalAO sampleSignalAO, TransformationConfiguration config) throws RuleException {

		final IInterpolationMethod interpolationMethod;
		switch (sampleSignalAO.getInterpolationPolicy()) {
		case CONSTANT:
			interpolationMethod = new ConstantInterpolation();
			break;
		case LINEAR:
			interpolationMethod = new LinearInterpolation();
			break;
		default:
			interpolationMethod = new ConstantInterpolation();
			break;
		}
		final long sampleInterval = sampleSignalAO.getBaseTimeUnit()
				.convert(sampleSignalAO.getSampleInterval().getTime(), sampleSignalAO.getSampleInterval().getUnit());
		defaultExecute(sampleSignalAO,
				new SampleSignalPO(sampleInterval, interpolationMethod, sampleSignalAO.getFillWithZeros()), config,
				true, true);
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
