package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.trigger.monitoring;

/**
 * Listener for Monitoring Thread in Monitoring Trigger
 * @author Carsten Cordes
 *
 */
public interface IMonitoringThreadListener {

	/**
	 * Called when Load Treshold is reached.
	 * @param cpuUsage Current Cpu Usage
	 * @param memUsage Current Mem Usage
	 * @param netUsage Current Net Usage
	 */
	public void notifyLoadBalancingTriggered(double cpuUsage, double memUsage, double netUsage);
}
