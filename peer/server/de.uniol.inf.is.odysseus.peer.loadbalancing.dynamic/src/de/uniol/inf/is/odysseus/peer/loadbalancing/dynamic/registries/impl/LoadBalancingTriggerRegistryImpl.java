package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingTrigger;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingTriggerRegistry;

public class LoadBalancingTriggerRegistryImpl extends AbstractInterfaceRegistry<ILoadBalancingTrigger> implements ILoadBalancingTriggerRegistry {

	@Override
	public void bindTrigger(ILoadBalancingTrigger serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindTrigger(ILoadBalancingTrigger serv) {
		unbindInstance(serv);
	}

	@Override
	public ILoadBalancingTrigger getTrigger(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isTriggerBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredTriggers() {
		return getRegisteredInstances();
	}

}
