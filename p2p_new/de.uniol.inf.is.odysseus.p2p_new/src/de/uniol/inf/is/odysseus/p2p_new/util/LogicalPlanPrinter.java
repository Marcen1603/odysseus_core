package de.uniol.inf.is.odysseus.p2p_new.util;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public class LogicalPlanPrinter {

	private final ILogicalOperator startOperator;

	public LogicalPlanPrinter(ILogicalQuery query) {
		Preconditions.checkNotNull(query, "Logical query to print must not be null!");
		startOperator = query.getLogicalPlan();
	}

	public final void printPlan() {
		System.out.println(toString());
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		printQuery(sb, startOperator);
		return sb.toString();
	}

	protected static String operatorToString(ILogicalOperator op) {
		return op.getClass().getSimpleName();
	}

	private static void printQuery(StringBuilder sb, ILogicalOperator top) {
		printOperator(sb, top, 0);
	}

	private static void printOperator(StringBuilder sb, ILogicalOperator op, int level) {
		for (int i = 0; i < level; i++) {
			sb.append("\t");
		}
		sb.append(operatorToString(op)).append("\n");

		for (LogicalSubscription sub : op.getSubscribedToSource()) {
			printOperator(sb, sub.getTarget(), level + 1);
		}
	}

}
