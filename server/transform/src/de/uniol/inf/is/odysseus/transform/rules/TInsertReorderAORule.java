package de.uniol.inf.is.odysseus.transform.rules;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.InputOrderRequirement;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ReOrderAO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalPlan;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * This rule adds a reorder operator in every case where inputOrderRequirement
 * is STRICT and input is outoforder
 * 
 * @author Marco Grawunder
 *
 */

public class TInsertReorderAORule extends AbstractTransformationRule<ILogicalOperator> {

	@Override
	public void execute(ILogicalOperator operator, TransformationConfiguration config) throws RuleException {
		// Avoid concurrent modification exceptions
		Collection<LogicalSubscription> subscriptions = new ArrayList<>(operator.getSubscribedToSource());
		
		for (LogicalSubscription subscription : subscriptions) {

			if (operator.getInputOrderRequirement(subscription.getSinkInPort()) == InputOrderRequirement.STRICT
					&& !subscription.getSchema().isInOrder()) {

				ReOrderAO reorder = new ReOrderAO();
				Collection<ILogicalOperator> toUpdate = LogicalPlan.insertOperator(reorder, operator,
						subscription.getSinkInPort(), 0, subscription.getSourceOutPort());
				
				insert(reorder);

				for (ILogicalOperator o : toUpdate) {
					update(o);
				}
			}
		}
	}

	@Override
	public boolean isExecutable(ILogicalOperator operator, TransformationConfiguration config) {
		for (int port = 0; port < operator.getNumberOfInputs(); port++) {
			if (operator.getInputSchema(port) != null && operator.getInputOrderRequirement(port) == InputOrderRequirement.STRICT
					&& !operator.getInputSchema(port).isInOrder()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.OUTOFORDER_INIT;
	}

}
