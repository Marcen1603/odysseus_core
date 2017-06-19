package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MovingObjectInterpolationAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.MovingObjectInterpolationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMovingObjectInterpolatorAOTransformRule extends AbstractTransformationRule<MovingObjectInterpolationAO> {

	@Override
	public void execute(MovingObjectInterpolationAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new MovingObjectInterpolationPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(MovingObjectInterpolationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
