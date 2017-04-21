package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.STDataStructureAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.STDataStructurePO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TSTDataStructureAOTransformRule extends AbstractTransformationRule<STDataStructureAO> {

	@Override
	public void execute(STDataStructureAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new STDataStructurePO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(STDataStructureAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super STDataStructureAO> getConditionClass() {
		return STDataStructureAO.class;
	}
}
