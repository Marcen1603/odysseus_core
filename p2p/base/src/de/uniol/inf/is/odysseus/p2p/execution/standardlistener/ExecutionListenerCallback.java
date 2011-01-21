package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerCallback;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class ExecutionListenerCallback implements IExecutionListenerCallback {

	private IExecutionListener executionListener;
	private P2PQuery query;

	public ExecutionListenerCallback() {
	}
	
	@Override
	public void setExecutionListener(IExecutionListener listener) {
		this.executionListener = listener;
		this.query = this.executionListener.getQuery();

	}
	
	@Override
	public synchronized void changeState(Lifecycle lifecycle) {
		this.executionListener.changeState(lifecycle);
	}

	@Override
	public P2PQuery getQuery() {
		return this.query;
	}
	
	

}
