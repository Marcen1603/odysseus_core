package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.failed;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class FailedExecutionHandler<F> extends AbstractExecutionHandler<AbstractPeer, F>{

	@Override
	public IExecutionHandler<AbstractPeer,F> clone()  {
		IExecutionHandler<AbstractPeer,F> handler = new FailedExecutionHandler<F>();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
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
