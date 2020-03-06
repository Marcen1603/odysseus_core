package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;
import java.util.Optional;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IOutOfOrderHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ReOrderAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * Cleanup case where a Reorder operator is added to the plan that is not longer
 * used, because the order has changed because of other reorder operators before
 * 
 * @author Marco Grawunder
 *
 */

public class TRemoveReorderAORule extends AbstractTransformationRule<ReOrderAO> {

	@Override
	public void execute(ReOrderAO operator, TransformationConfiguration config) throws RuleException {

		Collection<LogicalSubscription> sources = operator.getSubscribedToSource();
		if (sources.size() == 1) {
			LogicalSubscription sub = sources.iterator().next();
			ILogicalOperator target = sub.getSource();
			if (target instanceof IOutOfOrderHandler) {
				((IOutOfOrderHandler) target).setAssureOrder(true);
			}
		}

		Collection<ILogicalOperator> toUpdate = LogicalPlan.removeOperator(operator, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(operator);
	}

	@Override
	public boolean isExecutable(ReOrderAO operator, TransformationConfiguration config) {
		// Remove only, if added by the system (else the user explicitly wanted a reorder, e.g. even
		// in cases where out of order would be possible
		Optional<Object> addedBySystem = operator.getTransformationHint(ADDED_IN_TRANSFORMATION);
		if (addedBySystem.isPresent() && ((boolean) addedBySystem.get())) {
			Collection<LogicalSubscription> sources = operator.getSubscribedToSource();
			if (sources.size() == 1) {
				LogicalSubscription sub = sources.iterator().next();
				// use target and ask output schema for ordering! Ordering could change in the
				// operator that is not reflected inside the subscription ... Maybe we should
				// add a phase where we recalc
				// subscription schemata?
				ILogicalOperator target = sub.getSource();
				return target.getOutputSchema().isInOrder() || (target instanceof IOutOfOrderHandler
						&& ((IOutOfOrderHandler) target).isAssureOrder() == null);
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.OUTOFORDER_CLEANUP;
	}

}
