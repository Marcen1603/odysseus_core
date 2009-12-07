package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandler<T extends IPeer> extends Runnable {
	public Lifecycle getProvidedLifecycle();
	public void setCallbackExecutionListener(IExecutionListenerCallback executionListener);
	public IExecutionListenerCallback getCallbackExecutionListener();
	public Thread startHandler();
	public void setPeer(T peer);
	
}
