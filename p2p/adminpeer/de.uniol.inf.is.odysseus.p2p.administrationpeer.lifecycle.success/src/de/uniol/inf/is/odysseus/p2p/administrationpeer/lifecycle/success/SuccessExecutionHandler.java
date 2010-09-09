package de.uniol.inf.is.odysseus.p2p.administrationpeer.lifecycle.success;

import de.uniol.inf.is.odysseus.p2p.peer.AbstractPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class SuccessExecutionHandler<F> extends AbstractExecutionHandler<AbstractPeer, F> {

	@Override
	public IExecutionHandler<AbstractPeer, F> clone()  {
		IExecutionHandler<AbstractPeer, F> handler = new SuccessExecutionHandler<F>();
		handler.setFunction(getFunction());
		handler.setPeer(getPeer());
		handler.setExecutionListenerCallback(getExecutionListenerCallback());
		return handler;
	}

	@Override
	public String getName() {
		return "SuccessExecutionHandler";
	}

	@Override
	public void run() {
		Lifecycle priorLifecycle = getExecutionListenerCallback().getQuery().getHistory().get(getExecutionListenerCallback().getQuery().getHistory().size()-2);
		if(priorLifecycle == Lifecycle.NEW) {
			getExecutionListenerCallback().changeState(Lifecycle.SPLIT);
		} else if (priorLifecycle == Lifecycle.SPLIT) {
			getExecutionListenerCallback().changeState(Lifecycle.DISTRIBUTION);
		} else if (priorLifecycle == Lifecycle.DISTRIBUTION) {
			getExecutionListenerCallback().changeState(Lifecycle.GRANTED);
		} else if (priorLifecycle == Lifecycle.GRANTED) {
			getExecutionListenerCallback().changeState(Lifecycle.RUNNING);
		} else if (priorLifecycle == Lifecycle.RUNNING) {
			getExecutionListenerCallback().changeState(Lifecycle.TERMINATED);
		}
	}
	
	public SuccessExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.SUCCESS);
	}

}
