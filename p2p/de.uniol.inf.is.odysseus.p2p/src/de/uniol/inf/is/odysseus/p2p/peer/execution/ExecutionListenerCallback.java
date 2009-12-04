package de.uniol.inf.is.odysseus.p2p.peer.execution;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class ExecutionListenerCallback implements IExecutionListenerCallback {

	private IExecutionListener executionListener;
	private Query query;

	public ExecutionListenerCallback(IExecutionListener executionListener) {
		this.executionListener = executionListener;
		this.query = this.executionListener.getQuery();
	}
	
	@Override
	public synchronized void changeState(Lifecycle lifecycle) {
		this.executionListener.changeState(lifecycle);
	}

	@Override
	public Query getQuery() {
		return this.query;
	}

}
