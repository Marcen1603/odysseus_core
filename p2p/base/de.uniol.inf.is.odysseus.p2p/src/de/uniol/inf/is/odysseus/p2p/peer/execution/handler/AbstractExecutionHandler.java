package de.uniol.inf.is.odysseus.p2p.peer.execution.handler;

import de.uniol.inf.is.odysseus.p2p.peer.IOdysseusPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public abstract class AbstractExecutionHandler<F> implements IExecutionHandler<F>
{
	
	@Override
	public abstract IExecutionHandler<F> clone() ;

	@Override
	public void setProvidedLifecycle(Lifecycle lifecycle) {
		this.providedLifecycle = lifecycle;
	}
	
	
	@Override
	public abstract String getName();

	private Lifecycle providedLifecycle;
	private IExecutionListenerCallback executionListenerCallback = null;
	protected IOdysseusPeer peer;
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

	public AbstractExecutionHandler(AbstractExecutionHandler<F> other) {
		setFunction(other.getFunction());
		setPeer(other.getPeer());
		setExecutionListenerCallback(other.getExecutionListenerCallback());
		setProvidedLifecycle(other.getProvidedLifecycle());
	}
	
	public AbstractExecutionHandler(Lifecycle lifecycle, F function, IOdysseusPeer peer) {
		this.providedLifecycle = lifecycle;
		this.function = function;
		this.peer = peer;
	}
	
	public AbstractExecutionHandler() {
	}

	@Override
	public abstract void run();

	@Override
	public Thread startHandler() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}

	@Override
	public IOdysseusPeer getPeer() {
		return this.peer;
	}

	@Override
	public F getFunction() {
		return function;
	}
	
	@Override
	public void setPeer(IOdysseusPeer peer) {
		this.peer = peer;
	}
	
	@Override
	public void setFunction(F function) {
		this.function = function;
	}
}
