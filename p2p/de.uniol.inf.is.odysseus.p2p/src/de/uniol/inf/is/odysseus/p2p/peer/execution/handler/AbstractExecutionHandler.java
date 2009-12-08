package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public abstract class AbstractExecutionHandler<P, F> implements IExecutionHandler
{
	
	private Lifecycle providedLifecycle;
	private IExecutionListenerCallback executionListenerCallback = null;
	protected P peer;
	private F function;

	@Override
	public IExecutionListenerCallback getExecutionListenerCallback() {
		return this.executionListenerCallback;
	}
	
	@Override
	public void setExecutionListenerCallback(
			IExecutionListenerCallback callbackExecutionListener) {
		this.executionListenerCallback = callbackExecutionListener;
	}


	
	@Override
	public Lifecycle getProvidedLifecycle() {
		return this.providedLifecycle;
	}

	public AbstractExecutionHandler(Lifecycle lifecycle, F function, P peer) {
		this.providedLifecycle = lifecycle;
		this.function = function;
		this.peer = peer;
	}
	
	@Override
	public abstract void run();

	@Override
	public Thread startHandler() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}

	protected P getPeer() {
		return this.peer;
	}
	
	public F getFunction() {
		return function;
	}
	
}
