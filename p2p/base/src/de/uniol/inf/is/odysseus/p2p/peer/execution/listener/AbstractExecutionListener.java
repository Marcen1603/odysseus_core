package de.uniol.inf.is.odysseus.p2p.peer.execution.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public abstract class AbstractExecutionListener implements IExecutionListener {
	
	private P2PQuery query;
	private IExecutionListenerCallback callback;
	private Thread actualExecutionThread;
	

	public void setCallback(IExecutionListenerCallback callback) {
		this.callback = callback;
		getCallback().setExecutionListener(this);
	}
	
	public IExecutionListenerCallback getCallback() {
		return callback;
	}
	
	
	private Map<Lifecycle, IExecutionHandler<?>> handler = null;

	public AbstractExecutionListener(P2PQuery query) {
		this.query = query;
		this.handler = new HashMap<Lifecycle, IExecutionHandler<?>>();
	}
	
	@Override
	public synchronized void changeState(Lifecycle lifecycle) {
		getQuery().setStatus(lifecycle);
		synchronized (this) {
			this.notify();
		}

	}
	
	@Override
	public abstract void run();
	
	protected abstract void execute(IExecutionHandler<?> executionHandler);
	
	@Override
	public synchronized P2PQuery getQuery() {
		return query;
	}
	@Override
	public Thread startListener() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
	
	@Override
	public synchronized void registerHandler(IExecutionHandler<?> handler) {
		if(!getHandler().containsKey(handler.getProvidedLifecycle())) {
			getHandler().put(handler.getProvidedLifecycle(), handler);
			handler.setExecutionListenerCallback(getCallback());
		}
	}
	
	@Override
	public void registerHandler(
			List<IExecutionHandler<?>> handler) {
		for(IExecutionHandler<?> execHandler : handler) {
			registerHandler(execHandler);
		}
	}

	@Override
	public synchronized void deregisterHandler(Lifecycle lifecycle, IExecutionHandler<?> handler) {
		if(getHandler().containsKey(lifecycle)) {
			getHandler().remove(lifecycle);
		}
	}

	@Override
	public Map<Lifecycle, IExecutionHandler<?>> getHandler() {
		return handler;
	}
	
	protected synchronized Thread getActualExecutionThread() {
		return actualExecutionThread;
	}

	protected synchronized void setActualExecutionThread(Thread actualExecutionThread) {
		this.actualExecutionThread = actualExecutionThread;
	}
}
