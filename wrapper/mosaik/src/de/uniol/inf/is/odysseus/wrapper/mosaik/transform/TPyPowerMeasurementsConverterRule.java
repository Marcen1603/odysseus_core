package de.uniol.inf.is.odysseus.wrapper.mosaik.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.mosaik.logicaloperator.PyPowerMeasurementsConverterAO;
import de.uniol.inf.is.odysseus.wrapper.mosaik.physicaloperator.PyPowerMeasurementsConverterPO;

public class TPyPowerMeasurementsConverterRule extends AbstractTransformationRule<PyPowerMeasurementsConverterAO> {

	@Override
	public int getPriority() {
		return 11;
	}

	@Override
	public String getName() {
		return "PyPowerMeasurementsConverterAO --> PyPowerMeasurementsConverterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super PyPowerMeasurementsConverterAO> getConditionClass() {
		return PyPowerMeasurementsConverterAO.class;
	}

	@Override
	public void execute(PyPowerMeasurementsConverterAO operator, TransformationConfiguration config)
			throws RuleException {
		defaultExecute(operator, new PyPowerMeasurementsConverterPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(PyPowerMeasurementsConverterAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

}