package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.SpatialKNNAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.SpatialKNNPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSpatialKNNAOTransformRule extends AbstractTransformationRule<SpatialKNNAO> {

	@Override
	public void execute(SpatialKNNAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new SpatialKNNPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(SpatialKNNAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
