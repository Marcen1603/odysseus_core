package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandlerFactory {

	IExecutionHandler getNewInstance();

	Lifecycle getProvidedLifecycle();

	void setExecutionHandler(IExecutionHandler handler);

	String getName();

}
