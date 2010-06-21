package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution;

import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution.event.AbstractPlanExecutionEvent;

/**
 * IPlanModificationListener describes an object which will be informed if the
 * plan execution is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanExecutionListener {
	/**
	 * The method is called if the plan execution is modified. An
	 * IPlanExecutionListener can react to concrete an
	 * {@link AbstractPlanExecutionEvent}
	 * 
	 * @param eventArgs {@link AbstractPlanExecutionEvent} describes the event that occurs.
	 */
	public void planExecutionEvent(AbstractPlanExecutionEvent<?> eventArgs);
}
