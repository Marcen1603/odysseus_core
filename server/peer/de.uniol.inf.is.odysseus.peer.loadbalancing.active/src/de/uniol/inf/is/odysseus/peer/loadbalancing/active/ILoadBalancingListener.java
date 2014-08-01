package de.uniol.inf.is.odysseus.peer.loadbalancing.active;


/**
 * LoadBalancing Listener. Use registerLoadBalancingListener in  {@link ILoadBalancingCommunicator} to register Listener.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingListener {
	/**
	 * Called when LoadBalancing is finished.
	 */
	public void notifyLoadBalancingFinished();
}
