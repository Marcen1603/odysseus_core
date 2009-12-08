package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandlerFactory{
	public void setExecutionHandler(IExecutionHandler handler);
	public Lifecycle getProvidedLifecycle();
	public IExecutionHandler getNewInstance();
	public String getName();
}
