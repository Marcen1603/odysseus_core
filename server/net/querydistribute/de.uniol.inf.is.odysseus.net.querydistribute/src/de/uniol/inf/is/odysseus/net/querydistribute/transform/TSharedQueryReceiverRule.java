package de.uniol.inf.is.odysseus.net.querydistribute.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQueryReceiverAO;
import de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.SharedQueryReceiverPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSharedQueryReceiverRule extends AbstractTransformationRule<SharedQueryReceiverAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(SharedQueryReceiverAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new SharedQueryReceiverPO(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(SharedQueryReceiverAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SharedQueryReceiverAO --> SharedQueryReceiverPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
