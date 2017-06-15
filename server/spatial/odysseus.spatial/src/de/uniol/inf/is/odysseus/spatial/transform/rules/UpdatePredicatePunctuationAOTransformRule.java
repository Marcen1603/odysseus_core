package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.UpdatePredicatePunctuationAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.UpdatePredicatePunctuationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class UpdatePredicatePunctuationAOTransformRule
		extends AbstractTransformationRule<UpdatePredicatePunctuationAO> {

	@Override
	public void execute(UpdatePredicatePunctuationAO operator, TransformationConfiguration config)
			throws RuleException {
		defaultExecute(operator, new UpdatePredicatePunctuationPO(operator), config, true, true);

	}

	@Override
	public boolean isExecutable(UpdatePredicatePunctuationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
