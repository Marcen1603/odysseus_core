package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject.MOPredictionAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.MOPredictionPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class MOPredictionAOTransformRule extends AbstractTransformationRule<MOPredictionAO> {

	@Override
	public void execute(MOPredictionAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new MOPredictionPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(MOPredictionAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
