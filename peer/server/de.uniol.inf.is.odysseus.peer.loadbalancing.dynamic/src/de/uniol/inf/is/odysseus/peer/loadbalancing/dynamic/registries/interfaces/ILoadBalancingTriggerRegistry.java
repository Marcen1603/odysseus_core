package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.ILoadBalancingTrigger;

/**
 * Registry for Load Balancing triggers.
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingTriggerRegistry {
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void bindTrigger(ILoadBalancingTrigger serv);
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void unbindTrigger(ILoadBalancingTrigger serv);
	
	/**
	 * Returns trigger with specific Name
	 * @param name Name to look for
	 * @return Trigger if trigger with specified Name is found, null otherwise.
	 */
	public ILoadBalancingTrigger getTrigger(String name);
	
	/**
	 * Checks if trigger with specific name is bound by registry
	 * @param name Name to look for
	 * @return True, if trigger with specified name is bound
	 */
	public boolean isTriggerBound(String name);
	
	/**
	 * Gets list of all bound trigger names
	 * @return List of all bound Trigger names.
	 */
	public Set<String> getRegisteredTriggers();
}
