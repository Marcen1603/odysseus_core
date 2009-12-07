package de.uniol.inf.is.odysseus.p2p.execution.standardlistener;

import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListenerFactory;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public class ExecutionListenerFactory implements IExecutionListenerFactory {

	@Override
	public IExecutionListener getNewInstance(Query query, List<IExecutionHandler> handler) {
		IExecutionListener listener = new ExecutionListener(query);
		listener.registerHandler(handler);
		return listener;
	}

}
