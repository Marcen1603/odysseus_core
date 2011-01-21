package de.uniol.inf.is.odysseus.p2p.peer.execution.listener;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public interface IExecutionListenerCallback {
	public void setExecutionListener(IExecutionListener listener);
	public void changeState(Lifecycle lifecycle);
	public P2PQuery getQuery();
}
