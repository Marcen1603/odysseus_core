package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.QuerySourceAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAppendQueryAORule extends
		AbstractTransformationRule<QuerySourceAO> {

	@Override
	public void execute(QuerySourceAO operator,
			TransformationConfiguration config) throws RuleException {

		IPhysicalOperator op = operator.getOperator();
		int port = operator.getPort();

		Collection<LogicalSubscription> subs = operator.getSubscriptions();

		for (LogicalSubscription s : subs) {
			s.getSink().setPhysSubscriptionTo(op, s.getSinkInPort(), port,
					op.getOutputSchema());
		}
	}

	@Override
	public boolean isExecutable(QuerySourceAO operator,
			TransformationConfiguration config) {
		return true;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.ACCESS;
	}

}
