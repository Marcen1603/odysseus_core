package de.uniol.inf.is.odysseus.peer.loadbalancing.active;

import java.util.HashMap;
import java.util.Set;

public class LoadBalancingCommunicatorRegistry implements ILoadBalancingCommunicatorRegistry {

	HashMap<String,ILoadBalancingCommunicator> communicators = new HashMap<String,ILoadBalancingCommunicator>();
	
	
	public void bindCommunicator(ILoadBalancingCommunicator serv) {
		if(!communicators.containsKey(serv.getName())) {
			communicators.put(serv.getName(), serv);
		}
	}
	
	public void unbindCommunicator(ILoadBalancingCommunicator serv) {
		if(communicators.containsKey(serv.getName())) {
			communicators.remove(serv.getName());
		}
	}
	
	public ILoadBalancingCommunicator getCommunicator(String name) {
		return communicators.get(name);
	}
	
	public boolean isCommunicatorBound(String name) {
		return communicators.containsKey(name);
	}

	@Override
	public Set<String> getRegisteredCommunicators() {
		return communicators.keySet();
	}
	
	
}
