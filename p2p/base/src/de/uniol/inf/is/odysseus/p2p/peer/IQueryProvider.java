package de.uniol.inf.is.odysseus.p2p.peer;

import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public interface IQueryProvider {
	void addQuery(P2PQuery query);
	Set<String> getQueryIds();
	P2PQuery getQuery(String queryID);
	int getQueryCount();
	boolean hasQuery(String queryId);
	IExecutionListener getListenerForQuery(String queryID);
	void removeQuery(String queryId);
	int getQueryCount(Lifecycle lifecycle);
	int getQueryCount(List<Lifecycle> lifecycle);
}
