package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication;




/**
 * LoadBalancing Listener. Use registerLoadBalancingListener in  {@link ILoadBalancingCommunicator} to register Listener.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingListener {
	
	/***
	 * Called when LoadBalancing is finished.
	 * @param successful true if balancing was successful.
	 */
	public void notifyLoadBalancingFinished(boolean successful);
}
