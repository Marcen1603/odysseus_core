package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.communication.ILoadBalancingCommunicator;


/**
 * Registry for load balancing allocators
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingCommunicatorRegistry {
	
	/**
	 * Used by OSGi
	 * @param serv
	 */
	public void bindCommunicator(ILoadBalancingCommunicator serv);
	
	/**
	 * Used by OSGi
	 * @param serv
	 */
	public void unbindCommunicator(ILoadBalancingCommunicator serv);
	
	/**
	 * Gets Communicator by Name
	 * @param name name of Communciator
	 * @return Communicator with specified Name if found, null otherwise.
	 */
	public ILoadBalancingCommunicator getCommunicator(String name);
	
	/**
	 * Checks if Communicator is bound to registry.
	 * @param name Name of Communicator 
	 * @return true if Communciator with specified Name is bound
	 */
	public boolean isCommunicatorBound(String name);
	
	/**
	 * Returns list of registered Communicator Names.
	 * @return List of registered Communicator Names
	 */
	public Set<String> getRegisteredCommunicators();

}
