package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification;

import de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;

/**
 * IPlanModificationListener describes an object which will be informed if the
 * registered plan is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanModificationListener {
	/**
	 * The method is called if the registered plan is modified. An
	 * IPlanModificationListener can react to concrete
	 * {@link AbstractPlanModificationEvent} 
	 * 
	 * @param eventArgs {@link AbstractPlanModificationEvent} describes the event that occurs.
	 */
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs);
}
