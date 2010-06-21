package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event;

import de.uniol.inf.is.odysseus.base.planmanagement.event.AbstractEvent;
import de.uniol.inf.is.odysseus.planmanagement.executor.IPlanScheduling;

/**
 * AbstractPlanExecutionEvent is an Event that occurs during execution of
 * the registered execution plan. This is the base class for concrete
 * execution events.
 * 
 * An event consists of a sender, an id and a value.
 * 
 * @author Wolf Bauer
 * 
 * @param <ValueType>
 *            Type of the stored value which refers to the execution change.
 */
public abstract class AbstractPlanExecutionEvent<ValueType> extends
		AbstractEvent<IPlanScheduling, ValueType> {

	/**
	 * Constructor of AbstractPlanModificationEvent.
	 * 
	 * @param sender
	 *            Sender of this event.
	 * @param id
	 *            ID which describes which event occurs.
	 * @param value
	 *            value which refers to the execution change.
	 */
	public AbstractPlanExecutionEvent(IPlanScheduling sender, String id, ValueType value) {
		super(sender, id, value);
	}
}
