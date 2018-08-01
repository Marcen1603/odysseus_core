package de.uniol.inf.is.odysseus.temporaltypes.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.temporaltypes.logicaloperator.ChangeValidTimeAO;
import de.uniol.inf.is.odysseus.temporaltypes.physicaloperator.ChangeValidTimePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TransformChangeValidTimeRule extends AbstractTransformationRule<ChangeValidTimeAO> {

	@Override
	public void execute(ChangeValidTimeAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new ChangeValidTimePO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(ChangeValidTimeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public String getName() {
		return "ChangeValidTimeAO --> ChangeValidTimePO";
	}

}
