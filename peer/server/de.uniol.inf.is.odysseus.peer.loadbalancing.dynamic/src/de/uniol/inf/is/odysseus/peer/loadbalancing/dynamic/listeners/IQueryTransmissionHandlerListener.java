package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners;

/**
 * Listener for Query Transmission Handler
 * @author Carsten Cordes
 *
 */
public interface IQueryTransmissionHandlerListener {
	/**
	 * Called when all transmissions are finished.
	 */
	public void transmissionsFinished();
}
