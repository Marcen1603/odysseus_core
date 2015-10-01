package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.control;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.listeners.ILoadBalancingControllerListener;

public interface ILoadBalancingController {
	public boolean setLoadBalancingStrategy(String strategyName);
	public boolean isLoadBalancingRunning();
	public String getSelectedLoadBalancingStrategy();
	public void stopLoadBalancing();
	public void startLoadBalancing();
	public Set<String> getAvailableStrategies();
	
	public void forceLoadBalancing();
	
	public void addControllerListener(ILoadBalancingControllerListener listener);
	public void removeControllerListener(ILoadBalancingControllerListener listener);
}