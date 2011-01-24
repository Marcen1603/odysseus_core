package de.uniol.inf.is.odysseus.p2p.administrationpeer.jxta.strategy;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.IThinPeerBiddingStrategy;
import de.uniol.inf.is.odysseus.p2p.peer.IQueryProvider;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Lifecycle;
import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public class MaxQueryBiddingStrategyJxtaImpl implements IThinPeerBiddingStrategy {
	// Es soll nur geboten werden, wenn die Anzahl an Anfragen die schon
	// ueberwacht werden und die Anzahl an
	// Gebote die noch offen sind, kleiner 5 sind.
	private static int MAX_QUERYS = 10;
	final private IQueryProvider queryProvider;
	final private List<Lifecycle> interestedLifecycles;
	
	public MaxQueryBiddingStrategyJxtaImpl(IQueryProvider queryProvider) {
		this.queryProvider = queryProvider;
		interestedLifecycles = new ArrayList<Lifecycle>();
		interestedLifecycles.add(Lifecycle.DISTRIBUTION);
		interestedLifecycles.add(Lifecycle.RUNNING);
	}
	
	@Override
	public boolean bidding(P2PQuery q) {

			int i = queryProvider.getQueryCount(interestedLifecycles);
			if (i >= MAX_QUERYS) {
				return false;
			} else {
				return true;
			}
	}

}
