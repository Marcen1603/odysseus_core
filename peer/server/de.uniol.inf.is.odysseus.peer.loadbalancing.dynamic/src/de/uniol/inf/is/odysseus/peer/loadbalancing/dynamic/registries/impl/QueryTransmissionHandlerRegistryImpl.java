package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.impl;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQueryTransmissionHandler;
import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces.IQueryTransmissionHandlerRegistry;

public class QueryTransmissionHandlerRegistryImpl extends AbstractInterfaceRegistry<IQueryTransmissionHandler> implements IQueryTransmissionHandlerRegistry {

	@Override
	public void bindTransmissionHandler(IQueryTransmissionHandler serv) {
		bindInstance(serv);
		
	}

	@Override
	public void unbindTransmissionHandler(IQueryTransmissionHandler serv) {
		unbindInstance(serv);
		
	}

	@Override
	public IQueryTransmissionHandler getTransmissionHandler(String name) {
		return getInstance(name);
	}

	@Override
	public boolean isTransmissionHandlerBound(String name) {
		return isInstanceBound(name);
	}

	@Override
	public Set<String> getRegisteredTransmissionHandlers() {
		return getRegisteredInstances();
	}

}
