package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingAllocatorRegistry;

public class LoadBalancingAllocatorRegistry implements ILoadBalancingAllocatorRegistry {

	HashMap<String,ILoadBalancingAllocator> allocators = new HashMap<String,ILoadBalancingAllocator>();
	
	
	public void bindAllocator(ILoadBalancingAllocator serv) {
		if(!allocators.containsKey(serv.getName())) {
			allocators.put(serv.getName(), serv);
		}
	}
	
	public void unbindAllocator(ILoadBalancingAllocator serv) {
		if(allocators.containsKey(serv.getName())) {
			allocators.remove(serv.getName());
		}
	}
	
	public ILoadBalancingAllocator getAllocator(String name) {
		return allocators.get(name);
	}
	
	public boolean isAllocatorBound(String name) {
		return allocators.containsKey(name);
	}

	public Set<String> getRegisteredAllocators() {
		return allocators.keySet();
	}
	
	
}
