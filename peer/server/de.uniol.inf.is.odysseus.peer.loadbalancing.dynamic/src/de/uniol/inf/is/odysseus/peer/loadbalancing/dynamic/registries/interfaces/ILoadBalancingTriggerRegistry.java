package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingTrigger;

public interface ILoadBalancingTriggerRegistry {
	public void bindTrigger(ILoadBalancingTrigger serv);
	public void unbindTrigger(ILoadBalancingTrigger serv);
	public ILoadBalancingTrigger getTrigger(String name);
	public boolean isTriggerBound(String name);
	public Set<String> getRegisteredTriggers();
}
