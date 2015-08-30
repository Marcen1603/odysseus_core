package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic;

import java.util.Set;

public interface ILoadBalancingController {
	public boolean setLoadBalancingStrategy(String strategyName);
	public boolean setLoadBalancingAllocator(String allocatorName);
	public boolean isLoadBalancingRunning();
	public String getSelectedLoadBalancingStrategy();
	public String getSelectedLoadBalancingAllocator();
	public void stopLoadBalancing();
	public void startLoadBalancing();
	public Set<String> getAvailableStrategies();
	public Set<String> getAvailableAllocators();
	
	public void forceLoadBalancing();
	
	public void addControllerListener(ILoadBalancingControllerListener listener);
	public void removeControllerListener(ILoadBalancingControllerListener listener);
}
