package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandler extends Runnable {
	public Lifecycle getProvidedLifecycle();
	public void setExecutionListenerCallback(IExecutionListenerCallback executionListenerCallback);
	public IExecutionListenerCallback getExecutionListenerCallback();
	public Thread startHandler();
	public String getName();
	
}
