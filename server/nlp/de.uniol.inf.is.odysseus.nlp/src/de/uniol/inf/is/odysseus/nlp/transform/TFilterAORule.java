package de.uniol.inf.is.odysseus.nlp.transform;

import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.nlp.logicaloperator.FilterAO;
import de.uniol.inf.is.odysseus.nlp.physicaloperator.FilterPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TFilterAORule extends AbstractTransformationRule<FilterAO> {

	@Override
	public void execute(FilterAO operator, TransformationConfiguration config) throws RuleException {
		defaultExecute(operator, new FilterPO(operator.getFilterObject(), operator.getOptionMap()), config, true, true);	
	}

	@Override
	public boolean isExecutable(FilterAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
