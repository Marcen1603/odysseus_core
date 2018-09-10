package de.uniol.inf.is.odysseus.recommendation.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.recommendation.logicaloperator.UnnestRecommendationCandidatesAO;
import de.uniol.inf.is.odysseus.recommendation.physicaloperator.UnnestRecommendationCandidatesPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUnnestRecommendationCandidatesAORule extends AbstractTransformationRule<UnnestRecommendationCandidatesAO>{

	@Override
	public void execute(UnnestRecommendationCandidatesAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new UnnestRecommendationCandidatesPO<>(operator.getAttributePosition(), operator.getInputSchema()), config, true, true);
	}

	@Override
	public boolean isExecutable(UnnestRecommendationCandidatesAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
