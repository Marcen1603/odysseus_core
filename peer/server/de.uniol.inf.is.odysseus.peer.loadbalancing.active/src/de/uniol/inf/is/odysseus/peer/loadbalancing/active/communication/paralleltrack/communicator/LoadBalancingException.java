package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.paralleltrack.communicator;

/**
 * Exception used in LoadBalancing
 * @author Carsten Cordes
 *
 */
public class LoadBalancingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoadBalancingException (String message) {
		super(message);
	}
}
