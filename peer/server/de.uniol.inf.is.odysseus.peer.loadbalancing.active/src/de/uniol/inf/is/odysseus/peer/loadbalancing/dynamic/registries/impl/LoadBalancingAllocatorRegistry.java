package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingAllocatorRegistry;

public class LoadBalancingAllocatorRegistry extends AbstractInterfaceRegistry<ILoadBalancingAllocator> implements ILoadBalancingAllocatorRegistry {

	@Override
	public void bindAllocator(ILoadBalancingAllocator serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindAllocator(ILoadBalancingAllocator serv) {
		unbindInstance(serv);
		
	}

	@Override
	public ILoadBalancingAllocator getAllocator(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isAllocatorBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredAllocators() {
		return getRegisteredInstances();
	}

	
	
}
