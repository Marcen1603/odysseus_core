package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;


public interface ILoadBalancingQuerySelectorRegistry {
	public void bindSelector(IQuerySelector serv);
	public void unbindSelector(IQuerySelector serv);
	public IQuerySelector getSelector(String name);
	public boolean isSelectorBound(String name);
	public Set<String> getRegisteredSelectors();

}
