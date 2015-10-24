package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;


/**
 * Registry for Query Selectors in Load Balancing
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingQuerySelectorRegistry {
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void bindSelector(IQuerySelector serv);
	
	/**
	 * used by OSGi
	 * @param serv
	 */
	public void unbindSelector(IQuerySelector serv);
	
	/**
	 * Gets Selector with specific Name
	 * @param name name to look for.
	 * @return Selector with specific name if found, null otherwise.
	 */
	public IQuerySelector getSelector(String name);
	
	/**
	 * Checks if selector is bound to registry.
	 * @param name Name to look for.
	 * @return true if Selector is bound.
	 */
	public boolean isSelectorBound(String name);
	
	/**
	 * Returns list of registered Load Balancing selector names
	 * @return List of registered Load balancing selector Names.
	 */
	public Set<String> getRegisteredSelectors();

}
