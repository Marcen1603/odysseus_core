package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.granted;

import de.uniol.inf.is.odysseus.p2p.peer.communication.IMessageHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Subplan;

public class GrantedMessageHandler implements IMessageHandler, Runnable {

	private String namespace = null;
	private Query query;
	private IExecutionListenerCallback callback;
	
	public GrantedMessageHandler(Query query, IExecutionListenerCallback callback) {
		this.query = query;
		this.callback = callback;
	}
	
	@Override
	public String getInterestedNamespace() {
		return this.namespace;
	}

	@Override
	public void handleMessage(Object msg, String namespace) {
		
	}
	
	@Override
	public void setInterestedNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	@Override
	public void run() {
		synchronized (this) {
			try {
				this.wait(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(Subplan s : getQuery().getSubPlans().values()) {
				if(!(s.getStatus() == Lifecycle.RUNNING)) {
					getCallback().changeState(Lifecycle.FAILED);
					break;
				}
			}
			getCallback().changeState(Lifecycle.RUNNING);
		}
	}
	
	public IExecutionListenerCallback getCallback() {
		return callback;
	}
	
	public Query getQuery() {
		return query;
	}
}
