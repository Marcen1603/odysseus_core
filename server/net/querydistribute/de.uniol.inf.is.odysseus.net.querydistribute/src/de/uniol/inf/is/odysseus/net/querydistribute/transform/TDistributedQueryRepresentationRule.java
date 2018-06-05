package de.uniol.inf.is.odysseus.net.querydistribute.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.net.querydistribute.logicaloperator.DistributedQueryRepresentationAO;
import de.uniol.inf.is.odysseus.net.querydistribute.physicaloperator.DistributedQueryRepresentationPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TDistributedQueryRepresentationRule extends AbstractTransformationRule<DistributedQueryRepresentationAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(DistributedQueryRepresentationAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new DistributedQueryRepresentationPO(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(DistributedQueryRepresentationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "DistributedQueryRepresentationAO --> DistributedQueryRepresentationPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
