package de.uniol.inf.is.odysseus.p2p.operatorpeer.lifecycle.success;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.AbstractExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;

public class SuccessExecutionHandler<F> extends AbstractExecutionHandler<F> {


	public SuccessExecutionHandler() {
		super();
		setProvidedLifecycle(Lifecycle.SUCCESS);
	}

	public SuccessExecutionHandler(
			SuccessExecutionHandler<F> successExecutionHandler) {
		super(successExecutionHandler);
	}

	
	@Override
	public IExecutionHandler<F> clone()  {
		return new SuccessExecutionHandler<F>(this);
	}

	@Override
	public String getName() {
		return "SuccessExecutionHandler";
	}

	@Override
	public void run() {
		Lifecycle priorLifecycle = getExecutionListenerCallback().getQuery().getHistory().get(getExecutionListenerCallback().getQuery().getHistory().size()-2);

		if (priorLifecycle == Lifecycle.GRANTED) {
			getExecutionListenerCallback().changeState(Lifecycle.RUNNING);
		} else if (priorLifecycle == Lifecycle.RUNNING) {
			getExecutionListenerCallback().changeState(Lifecycle.TERMINATED);
		}
	}
	

}
