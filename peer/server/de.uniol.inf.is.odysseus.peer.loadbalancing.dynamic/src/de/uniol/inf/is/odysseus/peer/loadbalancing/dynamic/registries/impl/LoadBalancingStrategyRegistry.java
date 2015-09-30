package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingStrategyRegistry;

public class LoadBalancingStrategyRegistry extends AbstractInterfaceRegistry<ILoadBalancingStrategy> implements ILoadBalancingStrategyRegistry {

	@Override
	public void bindStrategy(ILoadBalancingStrategy serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindStrategy(ILoadBalancingStrategy serv) {
		unbindInstance(serv);
		
	}

	@Override
	public ILoadBalancingStrategy getStrategy(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isStrategyBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredStrategies() {
		return getRegisteredInstances();
	}

	
	
}
