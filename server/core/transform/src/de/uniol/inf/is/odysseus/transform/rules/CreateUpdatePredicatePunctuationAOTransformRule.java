package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.CreateUpdatePredicatePunctuationAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.CreateUpdatePredicatePunctuationPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class CreateUpdatePredicatePunctuationAOTransformRule
		extends AbstractTransformationRule<CreateUpdatePredicatePunctuationAO> {

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(CreateUpdatePredicatePunctuationAO operator, TransformationConfiguration config)
			throws RuleException {
		defaultExecute(operator, new CreateUpdatePredicatePunctuationPO(operator), config, true, true);

	}

	@Override
	public boolean isExecutable(CreateUpdatePredicatePunctuationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
