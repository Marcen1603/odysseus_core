package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.CreateUpdateExpressionsPunctuationAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.CreateUpdateExpressionsPunctuationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class CreateUpdateExpressionsPunctuationAOTransformRule
		extends AbstractTransformationRule<CreateUpdateExpressionsPunctuationAO> {

	@Override
	public void execute(CreateUpdateExpressionsPunctuationAO operator, TransformationConfiguration config)
			throws RuleException {
		defaultExecute(operator, new CreateUpdateExpressionsPunctuationPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(CreateUpdateExpressionsPunctuationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
