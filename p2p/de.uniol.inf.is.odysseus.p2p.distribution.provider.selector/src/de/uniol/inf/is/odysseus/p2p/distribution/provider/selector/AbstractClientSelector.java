package de.uniol.inf.is.odysseus.p2p.distribution.provider.selector;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;



public abstract class AbstractClientSelector implements IClientSelector {

	private IExecutionListenerCallback callback;
	private int time;

	public AbstractClientSelector(int time, IExecutionListenerCallback callback) {
		this.callback = callback;
		this.time = time;
	}
	
	@Override
	public void setTimetoWait(int time) {
		this.time = time;
	}

	@Override
	public abstract void run();

	
	@Override
	public IExecutionListenerCallback getCallback() {
		return this.callback;
	}
	
	public int getTime() {
		return time;
	}
}
