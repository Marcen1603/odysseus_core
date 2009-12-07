package de.uniol.inf.is.odysseus.p2p.peer.execution.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractExecutionListener implements IExecutionListener {
	
	private Query query;
	private IExecutionListenerCallback callback;
	
	public void setCallback(IExecutionListenerCallback callback) {
		this.callback = callback;
		getCallback().setExecutionListener(this);
	}
	
	public IExecutionListenerCallback getCallback() {
		return callback;
	}
	
	
	private Map<Lifecycle, IExecutionHandler<? extends IPeer>> handler = null;

	public AbstractExecutionListener(Query query) {
		this.query = query;
		this.handler = new HashMap<Lifecycle, IExecutionHandler<? extends IPeer>>();
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
	
	@Override
	public synchronized Query getQuery() {
		return query;
	}
	@Override
	public Thread startListener() {
		Thread t = new Thread(this);
		t.start();
		return t;
	}
	
	@Override
	public synchronized void registerHandler(IExecutionHandler<? extends IPeer> handler) {
		if(!getHandler().containsKey(handler.getProvidedLifecycle())) {
			getHandler().put(handler.getProvidedLifecycle(), handler);
			handler.setExecutionListenerCallback(getCallback());
		}
	}
	
	@Override
	public void registerHandler(
			List<IExecutionHandler<? extends IPeer>> handler) {
		for(IExecutionHandler<? extends IPeer> execHandler : handler) {
			registerHandler(execHandler);
		}
	}

	@Override
	public synchronized void deregisterHandler(Lifecycle lifecycle, IExecutionHandler<? extends IPeer> handler) {
		if(getHandler().containsKey(lifecycle)) {
			getHandler().remove(lifecycle);
		}
	}

	@Override
	public Map<Lifecycle, IExecutionHandler<? extends IPeer>> getHandler() {
		return handler;
	}
}
