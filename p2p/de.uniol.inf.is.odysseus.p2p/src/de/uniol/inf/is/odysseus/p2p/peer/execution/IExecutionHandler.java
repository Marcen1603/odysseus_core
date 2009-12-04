package de.uniol.inf.is.odysseus.p2p.peer.execution;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandler extends Runnable {
	public Lifecycle getProvidedLifecycle();
	public void setCallbackExecutionListener(IExecutionListenerCallback executionListener);
	public IExecutionListenerCallback getCallbackExecutionListener();
	public Thread startHandler();
}
