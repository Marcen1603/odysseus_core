package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.MODataStructureAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.MODataStructurePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TMODataStructureTransformRule extends AbstractTransformationRule<MODataStructureAO> {

	@Override
	public void execute(MODataStructureAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new MODataStructurePO<>(operator), config, true, true);

	}

	@Override
	public boolean isExecutable(MODataStructureAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
