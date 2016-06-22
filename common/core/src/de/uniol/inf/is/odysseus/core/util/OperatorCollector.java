package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;

/**
 * Helper class to collect operators from plan.
 * 
 * @author Michael Brand
 *
 */
public class OperatorCollector {

	/**
	 * Collects all operators from a logical plan.
	 */
	public static List<ILogicalOperator> collect(ILogicalOperator plan) {
		List<ILogicalOperator> ops = new ArrayList<>();
		collectRecursively(plan, ops);
		return ops;
	}

	/**
	 * Collects all operators from a logical plan recursively from top to
	 * sources.
	 * 
	 * @param op
	 *            The current operator to add and check.
	 * @param operators
	 *            All already collected operators.
	 */
	private static void collectRecursively(ILogicalOperator op, List<ILogicalOperator> ops) {
		ops.add(op);
		for (LogicalSubscription sub : op.getSubscribedToSource()) {
			collectRecursively(sub.getTarget(), ops);
		}
	}

}