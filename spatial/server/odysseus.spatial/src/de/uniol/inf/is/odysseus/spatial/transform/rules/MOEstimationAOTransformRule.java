package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOEstimationAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.MOEstimationPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class MOEstimationAOTransformRule extends AbstractTransformationRule<MOEstimationAO> {

	@Override
	public void execute(MOEstimationAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new MOEstimationPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(MOEstimationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
