package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.closed;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class ClosedExecutionHandler<F> extends AbstractExecutionHandler<AbstractPeer, F> {

	public ClosedExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.TERMINATED);
	}
	
	public IExecutionHandler<AbstractPeer,F> clone()  {
		IExecutionHandler<AbstractPeer,F> handler = new ClosedExecutionHandler<F>();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		handler.setProvidedLifecycle(getProvidedLifecycle());
		return handler;
	}

	@Override
	public String getName() {
		return "ClosedExecutionHandler";
	}

	@Override
	public void run() {
	}

}