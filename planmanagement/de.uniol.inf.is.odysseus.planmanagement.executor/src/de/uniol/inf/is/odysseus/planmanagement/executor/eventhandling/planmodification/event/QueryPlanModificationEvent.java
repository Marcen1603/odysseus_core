package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

public class QueryPlanModificationEvent extends
		AbstractPlanModificationEvent<IQuery> {

	public static String QUERY_REMOVE = "QUERY_REMOVE";
	public static String QUERY_START = "QUERY_START";
	public static String QUERY_STOP = "QUERY_STOP";
	public static String QUERY_REOPTIMIZE = "QUERY_REOPTIMIZE";

	public QueryPlanModificationEvent(IPlanManager sender, String id,
			IQuery value) {
		super(sender, id, value);
	}
}
