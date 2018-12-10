package de.uniol.inf.is.odysseus.relational_interval.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalPredicateWindowTIPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TRelationalPredicateWindowTIPO extends AbstractRelationalIntervalTransformationRule<AbstractWindowAO> {

	@Override
	public int getPriority() {
		return 10;
	}
	
	@Override
	public void execute(AbstractWindowAO operator, TransformationConfiguration config) throws RuleException {
		RelationalPredicateWindowTIPO window = new RelationalPredicateWindowTIPO(operator);
		defaultExecute(operator, window, config, true, true);
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AbstractWindowAO> getConditionClass() {
		return AbstractWindowAO.class;
	}

	
	

}
