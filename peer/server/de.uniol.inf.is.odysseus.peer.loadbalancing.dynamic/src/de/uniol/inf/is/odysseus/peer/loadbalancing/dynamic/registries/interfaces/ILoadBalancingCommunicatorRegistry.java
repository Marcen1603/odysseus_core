package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;


public interface ILoadBalancingCommunicatorRegistry {
	public void bindCommunicator(ILoadBalancingCommunicator serv);
	public void unbindCommunicator(ILoadBalancingCommunicator serv);
	public ILoadBalancingCommunicator getCommunicator(String name);
	public boolean isCommunicatorBound(String name);
	public Set<String> getRegisteredCommunicators();

}
