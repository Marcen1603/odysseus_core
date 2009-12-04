package de.uniol.inf.is.odysseus.p2p.peer.execution;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public abstract class AbstractExecutionListener implements IExecutionListener {
	


	private Query query;
	
	private Map<Lifecycle, IExecutionHandler> handler = null;

	public AbstractExecutionListener(Query query) {
		this.query = query;
		this.handler = new HashMap<Lifecycle, IExecutionHandler>();
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
	public synchronized void registerHandler(IExecutionHandler handler) {
		if(!getHandler().containsKey(handler.getProvidedLifecycle())) {
			getHandler().put(handler.getProvidedLifecycle(), handler);
			handler.setCallbackExecutionListener(new ExecutionListenerCallback(this));
		}
	}
	
	@Override
	public void registerHandler(
			List<IExecutionHandler> handler) {
		for(IExecutionHandler execHandler : handler) {
			registerHandler(execHandler);
		}
	}

	@Override
	public synchronized void deregisterHandler(Lifecycle lifecycle, IExecutionHandler handler) {
		if(getHandler().containsKey(lifecycle)) {
			getHandler().remove(lifecycle);
		}
	}

	@Override
	public Map<Lifecycle, IExecutionHandler> getHandler() {
		return handler;
	}
}
