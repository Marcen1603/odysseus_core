package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOTrajectoryPredictionAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.MOTrajectoryPredictionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class MovingObjectTrajectoryPredictionAOTransformRule
		extends AbstractTransformationRule<MOTrajectoryPredictionAO> {

	@Override
	public void execute(MOTrajectoryPredictionAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new MOTrajectoryPredictionPO<>(operator), config, true, true);

	}

	@Override
	public boolean isExecutable(MOTrajectoryPredictionAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
