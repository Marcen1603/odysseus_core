package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingStrategy;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingStrategyRegistry;

public class LoadBalancingStrategyRegistry implements ILoadBalancingStrategyRegistry {

	HashMap<String,ILoadBalancingStrategy> strategies = new HashMap<String,ILoadBalancingStrategy>();
	
	
	public void bindStrategy(ILoadBalancingStrategy serv) {
		if(!strategies.containsKey(serv.getName())) {
			strategies.put(serv.getName(), serv);
		}
	}
	
	public void unbindStrategy(ILoadBalancingStrategy serv) {
		if(strategies.containsKey(serv.getName())) {
			strategies.remove(serv.getName());
		}
	}
	
	public ILoadBalancingStrategy getStrategy(String name) {
		return strategies.get(name);
	}
	
	public boolean isStrategyBound(String name) {
		return strategies.containsKey(name);
	}

	public Set<String> getRegisteredStrategies() {
		return strategies.keySet();
	}
	
	
}
