package de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.strategy.bidding;

import de.uniol.inf.is.odysseus.p2p.operatorpeer.peerImpl.jxta.OperatorPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.operatorpeer.strategy.bidding.IBiddingStrategy;

public class MaxQueryBiddingStrategyJxtaImpl implements IBiddingStrategy {

	private static int MAXQUERIES = 10;

	public boolean doBidding(String query) {
		int i = 0;
		for (String s : OperatorPeerJxtaImpl.getInstance().getQueries()
				.keySet()) {
			Query q = OperatorPeerJxtaImpl.getInstance().getQueries().get(s);
			if (q.getStatus() == Query.Status.RUN) {
				i++;
			}

		}
		if (i >= MAXQUERIES)
			return false;
		else
			return true;
	}

}
