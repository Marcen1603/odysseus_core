package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.listener.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.jxta.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.Query;
import de.uniol.inf.is.odysseus.p2p.Query.Status;

public class MaxQueryBiddingStrategyJxtaImpl implements IThinPeerBiddingStrategy {
	// Es soll nur geboten werden, wenn die Anzahl an Anfragen die schon
	// überwacht werden und die Anzahl an
	// Gebote die noch offen sind, kleiner 5 sind.
	private static int MAX_QUERYS = 10;
	private HashMap<String, Query> queries;
	
	public HashMap<String, Query> getQueries() {
		return queries;
	}

	public MaxQueryBiddingStrategyJxtaImpl(HashMap<String, Query> queries) {
		this.queries = queries;
	}
	
	@Override
	public boolean bidding(Query q) {
		if (q.getStatus() == Status.OPEN) {

			int i = 0;
			for (String key : getQueries().keySet()) {
				QueryJxtaImpl query = (QueryJxtaImpl) getQueries().get(key);
				if (query.getStatus() == Status.BIDDING
						|| query.getStatus() == Status.RUN) {
					i++;
				}
			}
			if (i >= MAX_QUERYS) {
				// Nicht bieten
				q.setStatus(Status.NOBIDDING);
				return false;
			} else {
				q.setStatus(Status.BIDDING);
				return true;
			}
		}
		return false;
	}

}
