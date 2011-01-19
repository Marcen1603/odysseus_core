package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.terminated;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;

public class TerminatedExecutionHandler<F> extends AbstractExecutionHandler<F> {

	public TerminatedExecutionHandler(
			TerminatedExecutionHandler<F> closedExecutionHandler) {
		super(closedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone()  {
		return new TerminatedExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "ClosedExecutionHandler";
	}

	@Override
	public void run() {
	}

}
