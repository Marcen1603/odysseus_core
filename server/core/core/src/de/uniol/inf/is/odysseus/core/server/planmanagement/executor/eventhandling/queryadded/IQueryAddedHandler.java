package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.queryadded;

/**
 * IQueryAddedHandler describes an object which informs registered
 * {@link IQueryAddedListener} if a new query is added.
 * 
 * @author Michael Brand
 * 
 */
public interface IQueryAddedHandler {

	/**
	 * Registers an {@link IQueryAddedListener} to this handler.
	 * 
	 * @param listener
	 *            new {@link IQueryAddedListener}
	 */
	public void addQueryAddedListener(IQueryAddedListener listener);

	/**
	 * Unregisters an {@link IQueryAddedListener} at this handler.
	 * 
	 * @param listener
	 *            {@link IQueryAddedListener} to unregister.
	 */
	public void removeQueryAddedListener(IQueryAddedListener listener);
}