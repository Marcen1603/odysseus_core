package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingStrategyRegistry;

public class LoadBalancingStrategyRegistry implements ILoadBalancingStrategyRegistry {

	HashMap<String,ILoadBalancingStrategy> strategies = new HashMap<String,ILoadBalancingStrategy>();
	
	
	@Override
	public void bindStrategy(ILoadBalancingStrategy serv) {
		if(!strategies.containsKey(serv.getName())) {
			strategies.put(serv.getName(), serv);
		}
	}
	
	@Override
	public void unbindStrategy(ILoadBalancingStrategy serv) {
		if(strategies.containsKey(serv.getName())) {
			strategies.remove(serv.getName());
		}
	}
	
	@Override
	public ILoadBalancingStrategy getStrategy(String name) {
		return strategies.get(name);
	}
	
	@Override
	public boolean isStrategyBound(String name) {
		return strategies.containsKey(name);
	}

	@Override
	public Set<String> getRegisteredStrategies() {
		return strategies.keySet();
	}
	
	
}
