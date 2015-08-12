package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces.ILoadBalancingAllocatorRegistry;

public class LoadBalancingAllocatorRegistry implements ILoadBalancingAllocatorRegistry {

	HashMap<String,ILoadBalancingAllocator> allocators = new HashMap<String,ILoadBalancingAllocator>();
	
	
	@Override
	public void bindAllocator(ILoadBalancingAllocator serv) {
		if(!allocators.containsKey(serv.getName())) {
			allocators.put(serv.getName(), serv);
		}
	}
	
	@Override
	public void unbindAllocator(ILoadBalancingAllocator serv) {
		if(allocators.containsKey(serv.getName())) {
			allocators.remove(serv.getName());
		}
	}
	
	@Override
	public ILoadBalancingAllocator getAllocator(String name) {
		return allocators.get(name);
	}
	
	@Override
	public boolean isAllocatorBound(String name) {
		return allocators.containsKey(name);
	}

	@Override
	public Set<String> getRegisteredAllocators() {
		return allocators.keySet();
	}
	
	
}
