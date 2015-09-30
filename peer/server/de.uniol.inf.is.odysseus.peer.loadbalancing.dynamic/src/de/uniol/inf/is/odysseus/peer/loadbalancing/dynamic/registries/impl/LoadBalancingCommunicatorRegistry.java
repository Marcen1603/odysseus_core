package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingCommunicatorRegistry;

public class LoadBalancingCommunicatorRegistry extends AbstractInterfaceRegistry<ILoadBalancingCommunicator> implements ILoadBalancingCommunicatorRegistry {

	@Override
	public void bindCommunicator(ILoadBalancingCommunicator serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindCommunicator(ILoadBalancingCommunicator serv) {
		unbindInstance(serv);
		
	}

	@Override
	public ILoadBalancingCommunicator getCommunicator(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isCommunicatorBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredCommunicators() {
		return getRegisteredInstances();
	}

	
	
}
