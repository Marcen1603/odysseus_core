package de.uniol.inf.is.odysseus.p2p.peer.execution.listener;

import java.util.List;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IExecutionListenerFactory {
	public IExecutionListener getNewInstance(Query query, List<IExecutionHandler<?>> handler);
	public String getName();
}
