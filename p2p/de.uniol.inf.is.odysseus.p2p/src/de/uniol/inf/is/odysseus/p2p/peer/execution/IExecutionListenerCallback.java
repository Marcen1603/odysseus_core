package de.uniol.inf.is.odysseus.p2p.peer.execution;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;

public interface IExecutionListenerCallback {
	public void changeState(Lifecycle lifecycle);
	public Query getQuery();
}
