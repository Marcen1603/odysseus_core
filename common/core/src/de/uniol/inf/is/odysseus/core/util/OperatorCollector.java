package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.ControllablePhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;

/**
 * Helper class to collect operators and subscriptions from plan.
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
	 * Collects all operators from a logical plan.
	 */
	public static List<ILogicalOperator> collect(ILogicalPlan plan) {
		List<ILogicalOperator> ops = new ArrayList<>();
		collectRecursively(plan.getRoot(), ops);
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
			collectRecursively(sub.getSource(), ops);
		}
	}

	/**
	 * Collects all stateful operators from a physical plan.
	 */
	public static List<IStatefulPO> collect(List<IPhysicalOperator> roots) {
		List<IStatefulPO> ops = new ArrayList<>();
		for (IPhysicalOperator root : roots) {
			collectRecursively(root, ops);
		}
		return ops;
	}

	/**
	 * Collects all stateful operators from a physical plan recursively from top
	 * to sources.
	 * 
	 * @param op
	 *            The current operator to add and check.
	 * @param operators
	 *            All already collected operators.
	 */
	private static void collectRecursively(IPhysicalOperator op, List<IStatefulPO> ops) {
		if (op instanceof IStatefulPO) {
			ops.add((IStatefulPO) op);
		}

		if (op instanceof ISubscriber) {
			@SuppressWarnings("unchecked")
			ISubscriber<? extends IPhysicalOperator, ISubscription<ISource<IStreamObject<?>>,?>> sink = (ISubscriber<? extends IPhysicalOperator, ISubscription<ISource<IStreamObject<?>>,?>>) op;
			for (ISubscription<ISource<IStreamObject<?>>,?> sub : sink.getSubscribedToSource()) {
				collectRecursively(sub.getSource(), ops);
			}
		}
	}

	/**
	 * Collects all controllable physical subscriptions from a physical plan.
	 */
	public static List<ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?>> collectSubcriptions(
			List<IPhysicalOperator> roots) {
		List<ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?>> subs = new ArrayList<>();
		for (IPhysicalOperator root : roots) {
			collectSubscriptionsRecursively(root, subs);
		}
		return subs;
	}

	/**
	 * Collects all controllable physical subscriptions from a physical plan
	 * recursively from top to sources.
	 * 
	 * @param op
	 *            The current operator.
	 * @param operators
	 *            All already collected controllable subscriptions.
	 */
	private static void collectSubscriptionsRecursively(IPhysicalOperator op,
			List<ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?>> subs) {
		if (op instanceof ISubscriber) {
			@SuppressWarnings("unchecked")
			ISubscriber<? extends IPhysicalOperator, ISubscription<ISource<IStreamObject<?>>,?>> sink = (ISubscriber<? extends IPhysicalOperator, ISubscription<ISource<IStreamObject<?>>,?>>) op;
			for (ISubscription<ISource<IStreamObject<?>>,?> sub : sink.getSubscribedToSource()) {
				if (sub instanceof ControllablePhysicalSubscription) {
					subs.add((ControllablePhysicalSubscription<ISource<IStreamObject<?>>,?>) sub);
				}
				collectSubscriptionsRecursively(sub.getSource(), subs);
			}
		}
	}

}