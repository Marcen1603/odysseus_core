package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

/**
 * PlanModificationEvent is an event that occurs during modification of
 * the registered execution plan. It refers to an {@link IQuery}.
 * 
 * @author Wolf Bauer
 *
 */
public class QueryPlanModificationEvent extends
		AbstractPlanModificationEvent<IQuery> {

	/**
	 * ID for an event after removing a query.
	 */
	public static String QUERY_REMOVE = "QUERY_REMOVE";
	
	/**
	 * ID for an event after adding a query.
	 */
	public static String QUERY_ADDED = "QUERY_ADDED";
	
	/**
	 * ID for an event after starting a query.
	 */
	public static String QUERY_START = "QUERY_START";
	
	/**
	 * ID for an event after stopping a query.
	 */
	public static String QUERY_STOP = "QUERY_STOP";
	
	/**
	 * ID for an event after reoptimize a query.
	 */
	public static String QUERY_REOPTIMIZE = "QUERY_REOPTIMIZE";

	/**
	 * Constructor of QueryPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            {@link IQuery} to which this event refers.
	 */
	public QueryPlanModificationEvent(IPlanManager sender, String id,
			IQuery value) {
		super(sender, id, value);
	}
}
