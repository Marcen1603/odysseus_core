package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.terminated;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class TerminatedExecutionHandler<F> extends AbstractExecutionHandler<F> {

	public TerminatedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.TERMINATED);
	}
	
	public TerminatedExecutionHandler(
			TerminatedExecutionHandler<F> terminatedExecutionHandler) {
		super(terminatedExecutionHandler);
	}

	@Override
	public IExecutionHandler<F> clone()  {
		return new TerminatedExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "TerminatedExecutionHandler";
	}

	@Override
	public void run() {
	}

}