package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners;

/**
 * Listener for Load Balancing Trigger
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingTriggerListener {

	/**
	 * Called when a redistribution of queries is triggered.
	 * @param cpuLoadToRemove Cpu Load to remove.
	 * @param memLoadToRemove Mem Load to Remove
	 * @param netLoadToRemove Net Load to Remove.
	 */
	public void triggerLoadBalancing(double cpuLoadToRemove, double memLoadToRemove, double netLoadToRemove);
}
