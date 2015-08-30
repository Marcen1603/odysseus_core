package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.registries.interfaces;

import java.util.Set;

import de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.IQueryTransmissionHandler;

public interface IQueryTransmissionHandlerRegistry {
	public void bindTransmissionHandler(IQueryTransmissionHandler serv);
	public void unbindTransmissionHandler(IQueryTransmissionHandler serv);
	public IQueryTransmissionHandler getTransmissionHandler(String name);
	public boolean isTransmissionHandlerBound(String name);
	public Set<String> getRegisteredTransmissionHandlers();
}
