package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

/**
 * MultiQueryPlanModificationEvent is an event that occurs during modification of
 * the registered execution plan. It refers to a list of {@link IQuery}s.
 * 
 * @author Wolf Bauer
 *
 */
public class MultiQueryPlanModificationEvent extends
		AbstractPlanModificationEvent<ArrayList<IQuery>> {
	
	/**
	 * ID for an event after adding queries.
	 */
	public static String QUERIES_ADD = "QUERIES_ADD";
	
	/**
	 * Constructor of MultiQueryPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            List of {@link IQuery} to which this event refers.
	 */
	public MultiQueryPlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType,
			ArrayList<IQuery> value) {
		super(sender,  eventType, value);
	}
}
