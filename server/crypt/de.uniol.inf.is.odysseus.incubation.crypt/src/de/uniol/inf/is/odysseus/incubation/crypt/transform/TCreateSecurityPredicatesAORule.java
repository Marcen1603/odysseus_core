package de.uniol.inf.is.odysseus.incubation.crypt.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.incubation.crypt.predicates.ISecurityPredicate;
import de.uniol.inf.is.odysseus.incubation.crypt.predicates.SecurityPredicateFactory;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This Rule creates a SecurityPredicate for every Predicate and sets it instead
 * of the Predicate in every IHasPredicate and IHasPredicates. It will be run
 * every time, a operator starts.
 * 
 * @author MarkMilster
 *
 */
public class TCreateSecurityPredicatesAORule<P extends AbstractLogicalOperator> extends AbstractTransformationRule<P> {

	@Override
	public void execute(P operator, TransformationConfiguration config) throws RuleException {
		if (operator instanceof IHasPredicate) {
			IPredicate<?> predicate = ((IHasPredicate) operator).getPredicate();
			if (predicate instanceof ISecurityPredicate) {
				return;
			}
			ISecurityPredicate<?> securityPredicate = SecurityPredicateFactory
					.createSecurityPredicate(operator.getName(), predicate);
			((IHasPredicate) operator).setPredicate(securityPredicate);
		} else if (operator instanceof IHasPredicates) {
			List<IPredicate<?>> predicates = ((IHasPredicates) operator).getPredicates();
			List<IPredicate<?>> securityPredicates = new ArrayList<>();
			for (IPredicate<?> predicate : predicates) {
				securityPredicates.add(SecurityPredicateFactory.createSecurityPredicate(operator.getName(), predicate));
			}
			((IHasPredicates) operator).setPredicates(securityPredicates);
		}
	}

	@Override
	public boolean isExecutable(P operator, TransformationConfiguration config) {
		return operator instanceof IHasPredicates || operator instanceof IHasPredicate;
	}

	@Override
	public String getName() {
		return "Create SecurityPredicates";
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
