package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.PredictionEnrichAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.PredictionEnrichPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMovingObjectInterpolatorAOTransformRule extends AbstractTransformationRule<PredictionEnrichAO> {

	@Override
	public void execute(PredictionEnrichAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new PredictionEnrichPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(PredictionEnrichAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
