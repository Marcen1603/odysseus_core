package de.uniol.inf.is.odysseus.spatial.transform.rules;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.spatial.logicaloperator.UpdateableSelectAO;
import de.uniol.inf.is.odysseus.spatial.physicaloperator.UpdateableSelectPO;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class UpdateableSelectAOTransformRule extends AbstractTransformationRule<UpdateableSelectAO> {

	@Override
	public void execute(UpdateableSelectAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new UpdateableSelectPO(operator, operator.getPredicate()), config, true, true);
	}

	@Override
	public boolean isExecutable(UpdateableSelectAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
