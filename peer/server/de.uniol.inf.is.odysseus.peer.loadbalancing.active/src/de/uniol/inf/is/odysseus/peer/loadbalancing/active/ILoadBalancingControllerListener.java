package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.List;

public interface ILoadBalancingControllerListener {
	
	public void notifyLoadBalancingStatusChanged(boolean isRunning);
	
	public void notifyExcludedQueriesChanged(List<Integer> queryIDs);
	

}
