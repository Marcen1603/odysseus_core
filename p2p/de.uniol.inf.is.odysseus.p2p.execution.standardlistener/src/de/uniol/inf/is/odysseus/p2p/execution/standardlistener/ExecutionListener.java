package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.AbstractExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class ExecutionListener extends AbstractExecutionListener{
	

	public ExecutionListener(Query query) {
		super(query);
	}
	
	private boolean execute(Lifecycle lifecycle) {
		Thread executionThread = null;
		if(getHandler().containsKey(lifecycle)) {
			executionThread = new Thread(getHandler().get(lifecycle));
			executionThread.start();
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		while(getQuery().getStatus()!=Lifecycle.CLOSED) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(getHandler().containsKey(getQuery().getStatus())) {
				if(execute(getQuery().getStatus())) {
					continue;
				}
			}
		}
	}

}
