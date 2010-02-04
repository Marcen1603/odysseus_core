package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.closed;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;

public class ClosedExecutionHandler<F> extends AbstractExecutionHandler<AbstractPeer, F> {

	public IExecutionHandler<AbstractPeer,F> clone() throws CloneNotSupportedException {
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
