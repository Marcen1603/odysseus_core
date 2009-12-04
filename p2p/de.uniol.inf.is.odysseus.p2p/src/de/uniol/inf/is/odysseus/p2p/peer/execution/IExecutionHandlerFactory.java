package de.uniol.inf.is.odysseus.p2p.peer.execution;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandlerFactory extends Runnable{
	public Lifecycle getProvidedLifecycle();
	public IExecutionHandler getNewInstance();
}
