package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MovingObjectRangeAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.MovingObjectRangePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMovingObjectRangeAOTransformRule extends AbstractTransformationRule<MovingObjectRangeAO> {

	@Override
	public void execute(MovingObjectRangeAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new MovingObjectRangePO<>(operator), config, true, true);

	}

	@Override
	public boolean isExecutable(MovingObjectRangeAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
