package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public interface IExecutionHandler<F> extends Runnable, IClone {
	public void setProvidedLifecycle(Lifecycle lifecycle);
	public Lifecycle getProvidedLifecycle();
	public void setExecutionListenerCallback(IExecutionListenerCallback executionListenerCallback);
	public IExecutionListenerCallback getExecutionListenerCallback();
	public Thread startHandler();
	public String getName();
	public IExecutionHandler<F> clone() ;
	public void setFunction(F function);
	public F getFunction();
	public void setPeer(IOdysseusPeer peer);
	public IOdysseusPeer getPeer();
}
