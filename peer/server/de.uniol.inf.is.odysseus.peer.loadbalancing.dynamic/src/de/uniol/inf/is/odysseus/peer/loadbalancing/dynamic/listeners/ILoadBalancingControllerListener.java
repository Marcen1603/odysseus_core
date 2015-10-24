package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners;


/**
 * Listener for Load Balancing Control
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingControllerListener {
	
	/**
	 * Called when dynamic Load Balancing is actived or stopped.
	 * @param isRunning true if load Balancing is activated false if it is stopped.
	 */
	public void notifyLoadBalancingStatusChanged(boolean isRunning);
	
	

}
