package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingCommunicatorRegistry;

public class LoadBalancingCommunicatorRegistry implements ILoadBalancingCommunicatorRegistry {

	HashMap<String,ILoadBalancingCommunicator> communicators = new HashMap<String,ILoadBalancingCommunicator>();
	
	
	@Override
	public void bindCommunicator(ILoadBalancingCommunicator serv) {
		if(!communicators.containsKey(serv.getName())) {
			communicators.put(serv.getName(), serv);
		}
	}
	
	@Override
	public void unbindCommunicator(ILoadBalancingCommunicator serv) {
		if(communicators.containsKey(serv.getName())) {
			communicators.remove(serv.getName());
		}
	}
	
	@Override
	public ILoadBalancingCommunicator getCommunicator(String name) {
		return communicators.get(name);
	}
	
	@Override
	public boolean isCommunicatorBound(String name) {
		return communicators.containsKey(name);
	}

	@Override
	public Set<String> getRegisteredCommunicators() {
		return communicators.keySet();
	}
	
	
}
