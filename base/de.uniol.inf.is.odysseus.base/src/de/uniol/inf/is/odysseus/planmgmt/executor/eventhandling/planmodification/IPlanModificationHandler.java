package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification;

/**
 * IPlanModificationHandler describes an object which informs registered
 * {@link IPlanModificationListener} if the registered plan is modified.
 * 
 * @author Wolf Bauer
 * 
 */
public interface IPlanModificationHandler {
	/**
	 * Registers an {@link IPlanModificationListener} to this handler.
	 * 
	 * @param listener
	 *            new {@link IPlanModificationListener}
	 */
	public void addPlanModificationListener(IPlanModificationListener listener);

	/**
	 * Unregisters an {@link IPlanModificationListener} at this handler.
	 * 
	 * @param listener
	 *            {@link IPlanModificationListener} to unregister.
	 */
	public void removePlanModificationListener(
			IPlanModificationListener listener);
}
