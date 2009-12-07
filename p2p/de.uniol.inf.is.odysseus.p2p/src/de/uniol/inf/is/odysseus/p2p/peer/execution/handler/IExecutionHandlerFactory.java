package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandlerFactory{
	public Lifecycle getProvidedLifecycle();
	public IExecutionHandler getNewInstance();
}
