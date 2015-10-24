package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingTriggerListener;

/**
 * Provides methods to implement custom load balancing trigger, that monitors Peers and starts redistribution of Queries at runtime if neccessary.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingTrigger extends INamedInterface {
	
	/**
	 * Gets Name of current Trigger
	 * @see de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface#getName()
	 */
	public String getName();
	
	/**
	 * Add listener that is notified, when load balancing is triggered (e.g. a Strategy)
	 * @param listener Listener to add (e.g. Strategy) 
	 */
	public void addListener(ILoadBalancingTriggerListener listener);
	
	/**
	 * Remove listener that is notified, when load balancing is triggered
	 * @param listener Listener to remove
	 */
	public void removeListener(ILoadBalancingTriggerListener listener);
	
	/**
	 * Start Monitoring of Peers
	 */
	public void start();
	
	/**
	 * Stop Monitoring of Peers
	 */
	public void setInactive();
}
