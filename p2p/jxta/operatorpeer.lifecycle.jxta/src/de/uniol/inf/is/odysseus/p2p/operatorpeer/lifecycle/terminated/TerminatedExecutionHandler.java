package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.terminated;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class TerminatedExecutionHandler<F> extends AbstractExecutionHandler<F> {

	public TerminatedExecutionHandler(){
		super(Lifecycle.TERMINATED);
	}
	
	public TerminatedExecutionHandler(
			TerminatedExecutionHandler<F> closedExecutionHandler) {
		super(closedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone()  {
		return new TerminatedExecutionHandler<F>(this);
	}

	@Override
	public void run() {
	}

}
