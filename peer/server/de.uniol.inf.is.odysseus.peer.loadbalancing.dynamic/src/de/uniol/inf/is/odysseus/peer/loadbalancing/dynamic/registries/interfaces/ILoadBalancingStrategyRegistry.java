package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;

public interface ILoadBalancingStrategyRegistry {
	public void bindStrategy(ILoadBalancingStrategy serv);
	public void unbindStrategy(ILoadBalancingStrategy serv);
	public ILoadBalancingStrategy getStrategy(String name);
	public boolean isStrategyBound(String name);
	public Set<String> getRegisteredStrategies();

}
