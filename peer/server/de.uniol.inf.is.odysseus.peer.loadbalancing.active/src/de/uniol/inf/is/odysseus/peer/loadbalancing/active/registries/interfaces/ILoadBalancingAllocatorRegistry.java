package de.uniol.inf.is.odysseus.peer.loadbalancing.active.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.active.ILoadBalancingAllocator;

public interface ILoadBalancingAllocatorRegistry {
	public void bindAllocator(ILoadBalancingAllocator serv);
	public void unbindAllocator(ILoadBalancingAllocator serv);
	public ILoadBalancingAllocator getAllocator(String name);
	public boolean isAllocatorBound(String name);
	public Set<String> getRegisteredAllocators();

}
