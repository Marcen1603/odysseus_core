package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public abstract class AbstractExecutionHandler<T extends AbstractPeer> implements IExecutionHandler<T>
{
	
	private Lifecycle providedLifecycle;
	private IExecutionListenerCallback callbackExecutionListener = null;
	private T peer;

	@Override
	public IExecutionListenerCallback getCallbackExecutionListener() {
		return callbackExecutionListener;
	}

	@Override
	public void setCallbackExecutionListener(
			IExecutionListenerCallback callbackExecutionListener) {
		this.callbackExecutionListener = callbackExecutionListener;
	}


	
	@Override
	public Lifecycle getProvidedLifecycle() {
		return this.providedLifecycle;
	}

	public AbstractExecutionHandler(Lifecycle lifecycle) {
		this.providedLifecycle = lifecycle;
	}
	
	@Override
	public abstract void run();

	@Override
	public Thread startHandler() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}

	protected T getPeer() {
		return this.peer;
	}
}
