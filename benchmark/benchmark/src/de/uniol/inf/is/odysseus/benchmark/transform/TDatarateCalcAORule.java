package de.uniol.inf.is.odysseus.benchmark.transform;

import de.uniol.inf.is.odysseus.benchmark.logicaloperator.DatarateCalcAO;
import de.uniol.inf.is.odysseus.benchmark.physical.DatarateCalcPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatarateCalcAORule extends AbstractTransformationRule<DatarateCalcAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(DatarateCalcAO operator,
			TransformationConfiguration config) {
		@SuppressWarnings("rawtypes")
		DatarateCalcPO po = new DatarateCalcPO<>();
		po.setUpdateRate(operator.getUpdateRate());
		defaultExecute(operator, po, config, true, false);
	}

	@Override
	public boolean isExecutable(DatarateCalcAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DatarateCalcAO --> DatarateCalcPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super DatarateCalcAO> getConditionClass() {
		return DatarateCalcAO.class;
	}
}
