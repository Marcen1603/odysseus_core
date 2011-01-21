package de.uniol.inf.is.odysseus.p2p.peer.execution.listener;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public interface IExecutionListener extends Runnable{
	public Thread startListener();
	public void registerHandler(IExecutionHandler<?> handler);
	public void registerHandler(List<IExecutionHandler<?>> handler);
	public void deregisterHandler(Lifecycle lifecycle, IExecutionHandler<?> handler);
	public void changeState(Lifecycle lifecycle);
	public Map<Lifecycle, IExecutionHandler<?>> getHandler();
	public P2PQuery getQuery();
}
