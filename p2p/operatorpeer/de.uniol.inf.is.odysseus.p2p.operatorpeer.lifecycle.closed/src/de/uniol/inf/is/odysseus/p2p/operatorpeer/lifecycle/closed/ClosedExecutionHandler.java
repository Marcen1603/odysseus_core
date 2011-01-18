package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.closed;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;

public class ClosedExecutionHandler<F> extends AbstractExecutionHandler<F> {

	public ClosedExecutionHandler(
			ClosedExecutionHandler<F> closedExecutionHandler) {
		super(closedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone()  {
		return new ClosedExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "ClosedExecutionHandler";
	}

	@Override
	public void run() {
	}

}
