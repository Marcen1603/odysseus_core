package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners;


public interface ILoadBalancingControllerListener {
	
	public void notifyLoadBalancingStatusChanged(boolean isRunning);
	
	

}
