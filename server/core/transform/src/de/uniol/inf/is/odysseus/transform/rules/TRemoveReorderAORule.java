package de.uniol.inf.is.odysseus.transform.rules;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IOutOfOrderHandler;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ReOrderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
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
			ILogicalOperator target = sub.getTarget();
			if (target instanceof IOutOfOrderHandler) {
				((IOutOfOrderHandler) target).setAssureOrder(true);
			}
		}
		
		Collection<ILogicalOperator> toUpdate = RestructHelper.removeOperator(operator, true);
		for (ILogicalOperator o : toUpdate) {
			update(o);
		}
		retract(operator);
	}

	@Override
	public boolean isExecutable(ReOrderAO operator, TransformationConfiguration config) {
		Collection<LogicalSubscription> sources = operator.getSubscribedToSource();
		if (sources.size() == 1) {
			LogicalSubscription sub = sources.iterator().next();
			// use target and ask output schema for ordering! Ordering could change in the
			// operator that is not reflected inside the subscription ... Maybe we should add a phase where we recalc 
			// subscription schemata?
			ILogicalOperator target = sub.getTarget();
			return target.getOutputSchema().isInOrder() || (target instanceof IOutOfOrderHandler);
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.OUTOFORDER_CLEANUP;
	}

}
