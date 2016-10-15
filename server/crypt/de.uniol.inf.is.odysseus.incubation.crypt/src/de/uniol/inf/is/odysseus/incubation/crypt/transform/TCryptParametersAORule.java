package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author MarkMilster
 *
 */
public class TCryptParametersAORule<P extends AbstractLogicalOperator> extends AbstractTransformationRule<P> {

	@Override
	public void execute(P operator, TransformationConfiguration config) throws RuleException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isExecutable(P operator, TransformationConfiguration config) {
		return operator instanceof IHasPredicates || operator instanceof IHasPredicate;
	}

	@Override
	public String getName() {
		return "Encrypt Predicates";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.INIT;
	}

	@Override
	public Class<? super AbstractLogicalOperator> getConditionClass() {
		return AbstractLogicalOperator.class;
	}

}
