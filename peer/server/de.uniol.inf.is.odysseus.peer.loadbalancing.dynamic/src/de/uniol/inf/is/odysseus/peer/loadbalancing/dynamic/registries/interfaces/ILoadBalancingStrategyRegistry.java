package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingStrategy;

/**
 * Regsitry for Load Balancing Strategies that implement dynamic Load Balancing methods.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingStrategyRegistry {
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void bindStrategy(ILoadBalancingStrategy serv);
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void unbindStrategy(ILoadBalancingStrategy serv);
	
	/**
	 * Returns Strategy with specific Name
	 * @param name Name to look for
	 * @return Strategy if name is found, null otherwise
	 */
	public ILoadBalancingStrategy getStrategy(String name);
	
	/**
	 * Checks if Strategy with specific name is bound to Registry
	 * @param name Name to look for
	 * @return true if Strategy with particular name is found.
	 */
	public boolean isStrategyBound(String name);
	
	/**
	 * Returns List of all registered Strategy Names
	 * @return List of all registered Strategy Names
	 */
	public Set<String> getRegisteredStrategies();

}
