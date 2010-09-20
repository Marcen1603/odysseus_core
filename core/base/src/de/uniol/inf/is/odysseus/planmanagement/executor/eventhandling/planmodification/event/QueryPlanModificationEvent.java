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
	 * Constructor of QueryPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            {@link IQuery} to which this event refers.
	 */
	public QueryPlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType, IQuery value) {
		super(sender, eventType, value);
	}
}
