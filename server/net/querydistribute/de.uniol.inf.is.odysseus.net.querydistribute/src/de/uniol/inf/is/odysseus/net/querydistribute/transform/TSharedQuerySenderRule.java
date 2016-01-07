package de.uniol.inf.is.odysseus.net.querydistribute.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.SharedQuerySenderAO;
import de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.SharedQuerySenderPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSharedQuerySenderRule extends AbstractTransformationRule<SharedQuerySenderAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(SharedQuerySenderAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new SharedQuerySenderPO(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(SharedQuerySenderAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "SharedQuerySenderAO --> SharedQuerySenderPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
