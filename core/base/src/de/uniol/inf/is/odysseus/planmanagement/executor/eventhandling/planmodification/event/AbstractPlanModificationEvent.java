package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.event.AbstractEvent;
import de.uniol.inf.is.odysseus.event.IEventType;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanManager;

/**
 * AbstractPlanModificationEvent is an Event that occurs during modification of
 * the registered execution plan. This is the base class for concrete
 * modification events.
 * 
 * An event consists of a sender, an id and a value.
 * 
 * @author Wolf Bauer
 * 
 * @param <ValueType>
 *            Type of the stored value which refers to the modification.
 */
public abstract class AbstractPlanModificationEvent<ValueType> extends
		AbstractEvent<IPlanManager, ValueType> {

	/**
	 * Constructor of AbstractPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            value which refers to the modification.
	 */
	public AbstractPlanModificationEvent(IPlanManager sender, PlanModificationEventType eventType,
			ValueType value) {
		super(sender, eventType, value);
	}
}
