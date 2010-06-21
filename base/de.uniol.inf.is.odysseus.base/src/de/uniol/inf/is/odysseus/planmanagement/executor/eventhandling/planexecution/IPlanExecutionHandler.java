package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planexecution;

/**
 * IPlanExecutionHandler describes an object which informs registered
 * {@link IPlanExecutionListener} if the registered plan is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanExecutionHandler {
	/**
	 * Registers an {@link IPlanExecutionListener} to this handler.
	 * 
	 * @param listener
	 *            new {@link IPlanExecutionListener}
	 */
	public void addPlanExecutionListener(IPlanExecutionListener listener);

	/**
	 * Unregisters an {@link IPlanExecutionListener} at this handler.
	 * 
	 * @param listener
	 *            {@link IPlanExecutionListener} to unregister.
	 */
	public void removePlanExecutionListener(IPlanExecutionListener listener);
}
