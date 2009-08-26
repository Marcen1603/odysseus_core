package de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;

public class QueryEndEvent extends AbstractEndEvent<IEditableQuery> {

	public static String QUERY_REMOVE = "QUERY_REMOVED";
	public static String QUERY_START = "QUERY_STARTED";
	public static String QUERY_STOP = "QUERY_STOPPED";
	public static String QUERY_REOPTIMIZE = "QUERY_REOPTIMIZE";

	public QueryEndEvent(IOptimizer sender, String id, IEditableQuery value,
			IExecutionPlan newExecutionPlan) {
		super(sender, id, value, newExecutionPlan);
	}
}
