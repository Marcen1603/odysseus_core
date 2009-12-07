package de.uniol.inf.is.odysseus.p2p.peer.execution.listener;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.p2p.peer.IPeer;
import de.uniol.inf.is.odysseus.p2p.peer.execution.handler.IExecutionHandler;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IExecutionListener extends Runnable{
	public Thread startListener();
	public void registerHandler(IExecutionHandler<? extends IPeer> handler);
	public void registerHandler(List<IExecutionHandler<? extends IPeer>> handler);
	public void deregisterHandler(Lifecycle lifecycle, IExecutionHandler<? extends IPeer> handler);
	public void changeState(Lifecycle lifecycle);
	public Map<Lifecycle, IExecutionHandler<? extends IPeer>> getHandler();
	public Query getQuery();
}
