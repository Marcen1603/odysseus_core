package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandlerFactory{
	public void setExecutionHandler(IExecutionHandler<? extends IPeer> handler);
	public Lifecycle getProvidedLifecycle();
	public IExecutionHandler<? extends IPeer> getNewInstance();
}
