package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

public abstract class AbstractInterfaceRegistry<T extends INamedInterface>{

	HashMap<String,T> registeredInstances = new HashMap<String,T>();
	
	protected void bindInstance(T serv) {
		if(!registeredInstances.containsKey(serv.getName())) {
			registeredInstances.put(serv.getName(), serv);
		}
	}
	
	protected void unbindInstance(T serv) {
		if(registeredInstances.containsKey(serv.getName())) {
			registeredInstances.remove(serv.getName());
		}
	}
	
	public T getInstance(String name) {
		return registeredInstances.get(name);
	}
	
	public boolean isInstanceBound(String name) {
		return registeredInstances.containsKey(name);
	}

	public Set<String> getRegisteredInstances() {
		return registeredInstances.keySet();
	}
	
	
}
