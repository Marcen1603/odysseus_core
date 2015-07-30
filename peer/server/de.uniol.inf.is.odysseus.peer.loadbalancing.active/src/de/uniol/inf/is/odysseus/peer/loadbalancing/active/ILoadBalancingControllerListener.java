package de.uniol.inf.is.odysseus.peer.loadbalancing.active;


public interface ILoadBalancingControllerListener {
	
	public void notifyLoadBalancingStatusChanged(boolean isRunning);
	
	

}
