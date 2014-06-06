package de.uniol.inf.is.odysseus.datarate.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.datarate.logicaloperator.DatarateAO;
import de.uniol.inf.is.odysseus.datarate.physicaloperator.DataratePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDatarateRule extends AbstractTransformationRule<DatarateAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(DatarateAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new DataratePO(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(DatarateAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DatarateAO --> DataratePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
