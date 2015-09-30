package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;

public interface ILoadBalancingAllocatorRegistry {
	public void bindAllocator(ILoadBalancingAllocator serv);
	public void unbindAllocator(ILoadBalancingAllocator serv);
	public ILoadBalancingAllocator getAllocator(String name);
	public boolean isAllocatorBound(String name);
	public Set<String> getRegisteredAllocators();

}
