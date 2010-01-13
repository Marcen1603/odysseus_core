package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandler<P,F> extends Runnable {
	public void setProvidedLifecycle(Lifecycle lifecycle);
	public Lifecycle getProvidedLifecycle();
	public void setExecutionListenerCallback(IExecutionListenerCallback executionListenerCallback);
	public IExecutionListenerCallback getExecutionListenerCallback();
	public Thread startHandler();
	public String getName();
	public IExecutionHandler<P,F> clone() throws CloneNotSupportedException;
	public void setFunction(F function);
	public F getFunction();
	public void setPeer(P peer);
	public P getPeer();
	
}
