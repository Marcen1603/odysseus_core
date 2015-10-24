package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingAllocator;

/**
 * Registry for Load Balancing Allocators.
 * @author badagent
 *
 */
public interface ILoadBalancingAllocatorRegistry {
	/**
	 * Used by OSGi
	 * @param serv Instance
	 */
	public void bindAllocator(ILoadBalancingAllocator serv);
	
	/**
	 * Used by OSGi
	 * @param serv instance
	 */
	public void unbindAllocator(ILoadBalancingAllocator serv);
	
	/**
	 * Gets registered Allocator by name.
	 * @param name Name to look for.
	 * @return Allocator if it is found, null otherwise.
	 */
	public ILoadBalancingAllocator getAllocator(String name);
	
	/**
	 * Checks if Allocator with name is bound to registry.
	 * @param name Name to look for.
	 * @return true if allocator is bound.
	 */
	public boolean isAllocatorBound(String name);
	
	/**
	 * Returns List of all bound allocator names.
	 * @return List of all bound allocator names.
	 */
	public Set<String> getRegisteredAllocators();

}
