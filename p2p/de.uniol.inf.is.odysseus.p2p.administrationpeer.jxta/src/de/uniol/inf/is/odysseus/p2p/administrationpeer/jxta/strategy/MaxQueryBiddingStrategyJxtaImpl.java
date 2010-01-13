package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Query;
import de.uniol.inf.is.odysseus.p2p.peer.execution.listener.IExecutionListener;

public class MaxQueryBiddingStrategyJxtaImpl implements IThinPeerBiddingStrategy {
	// Es soll nur geboten werden, wenn die Anzahl an Anfragen die schon
	// überwacht werden und die Anzahl an
	// Gebote die noch offen sind, kleiner 5 sind.
	private static int MAX_QUERYS = 10;
	private HashMap<Query, IExecutionListener> queries;
	
	public HashMap<Query, IExecutionListener> getQueries() {
		return queries;
	}

	public MaxQueryBiddingStrategyJxtaImpl(HashMap<Query, IExecutionListener> hashMap) {
		this.queries = hashMap;
	}
	
	@Override
	public boolean bidding(Query q) {

			int i = 0;
			for (Query key : getQueries().keySet()) {
				QueryJxtaImpl query = (QueryJxtaImpl) key;
				if (query.getStatus() == Lifecycle.DISTRIBUTION
						|| query.getStatus() == Lifecycle.RUNNING) {
					i++;
				}
			}
			if (i >= MAX_QUERYS) {
				// Nicht bieten
//				q.setStatus(Status.NOBIDDING);
				return false;
			} else {
//				q.setStatus(Status.BIDDING);
				return true;
			}
	}

}
