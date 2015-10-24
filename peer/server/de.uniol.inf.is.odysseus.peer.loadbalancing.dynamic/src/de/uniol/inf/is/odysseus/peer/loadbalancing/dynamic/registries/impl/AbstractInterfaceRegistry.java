package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.HashMap;
import java.util.Set;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

/**
 * Abstract Registry class that allows for fast implementation of Component Registries in dynamic load balancing
 * @author Carsten Cordes
 *
 * @param <T> Type of Component for this registry
 */
public abstract class AbstractInterfaceRegistry<T extends INamedInterface>{

	HashMap<String,T> registeredInstances = new HashMap<String,T>();
	
	/**
	 * Used to bind Component. Map this to OSGi binding.
	 * @param serv Component to bind.
	 */
	protected void bindInstance(T serv) {
		if(!registeredInstances.containsKey(serv.getName())) {
			registeredInstances.put(serv.getName(), serv);
		}
	}
	
	/**
	 * Used to unbind Component. Map this to OSGi unbind
	 * @param serv Component to unbind.
	 */
	protected void unbindInstance(T serv) {
		if(registeredInstances.containsKey(serv.getName())) {
			registeredInstances.remove(serv.getName());
		}
	}
	
	/**
	 * Get Istance by name 
	 * @param name Name of Instance to get.
	 * @return Instance with specified name or null.
	 */
	public T getInstance(String name) {
		return registeredInstances.get(name);
	}
	
	/**
	 * Checks if Instance with particular Name is registered in Registry.
	 * @param name Name of Instance to look for.
	 * @return true if instance is bound.
	 */
	public boolean isInstanceBound(String name) {
		return registeredInstances.containsKey(name);
	}

	/**
	 * Returns list of all registered Instance Names
	 * @return List of Registered instance Names.
	 */
	public Set<String> getRegisteredInstances() {
		return registeredInstances.keySet();
	}
	
	
}
