package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.failed;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class FailedExecutionHandler<F> extends AbstractExecutionHandler<F> {

	public FailedExecutionHandler(
			FailedExecutionHandler<F> failedExecutionHandler) {
		super(failedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone() {
		return new FailedExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "FailedExecutionHandler";
	}

	@Override
	public void run() {
		getExecutionListenerCallback().changeState(Lifecycle.TERMINATED);
	}

}
