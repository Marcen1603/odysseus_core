package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQuerySelector;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.ILoadBalancingQuerySelectorRegistry;

public class QuerySelectorRegistryImpl extends AbstractInterfaceRegistry<IQuerySelector> implements ILoadBalancingQuerySelectorRegistry {

	@Override
	public void bindSelector(IQuerySelector serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindSelector(IQuerySelector serv) {
		unbindInstance(serv);
		
	}

	@Override
	public IQuerySelector getSelector(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isSelectorBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredSelectors() {
		return getRegisteredInstances();
	}

	
}
