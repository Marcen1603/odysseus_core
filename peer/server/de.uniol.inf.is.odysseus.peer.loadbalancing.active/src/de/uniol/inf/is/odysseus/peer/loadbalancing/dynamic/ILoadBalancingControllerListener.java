package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;


public interface ILoadBalancingControllerListener {
	
	public void notifyLoadBalancingStatusChanged(boolean isRunning);
	
	

}
