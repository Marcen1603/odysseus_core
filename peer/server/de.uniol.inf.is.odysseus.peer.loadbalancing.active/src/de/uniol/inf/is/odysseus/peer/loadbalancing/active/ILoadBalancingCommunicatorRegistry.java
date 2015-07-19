package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.Set;

public interface ILoadBalancingCommunicatorRegistry {
	public void bindCommunicator(ILoadBalancingCommunicator serv);
	public void unbindCommunicator(ILoadBalancingCommunicator serv);
	public ILoadBalancingCommunicator getCommunicator(String name);
	public boolean isCommunicatorBound(String name);
	public Set<String> getRegisteredCommunicators();

}
