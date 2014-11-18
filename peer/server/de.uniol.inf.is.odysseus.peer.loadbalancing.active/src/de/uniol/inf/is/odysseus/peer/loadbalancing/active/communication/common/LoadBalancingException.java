package de.uniol.inf.is.odysseus.peer.loadbalancing.active.communication.common;

/**
 * Exception used in LoadBalancing
 * 
 * @author Carsten Cordes
 *
 */
public class LoadBalancingException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Get Message of Exception.
	 * @param message
	 */
	public LoadBalancingException(String message) {
		super(message);
	}
}
