package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlaceHolderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.PlaceHolderPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPlaceHolderAORule extends AbstractTransformationRule<PlaceHolderAO> {

	@Override
	public void execute(PlaceHolderAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new PlaceHolderPO<>(operator), config, true, true);
	}

	@Override
	public boolean isExecutable(PlaceHolderAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super PlaceHolderAO> getConditionClass() {
		return PlaceHolderAO.class;
	}
	
}
