package de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.strategy.bidding;

import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.AdministrationPeerJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.peerImpl.jxta.queryAdministration.QueryJxtaImpl;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Query;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.queryAdministration.Query.Status;
import de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy.bidding.IBiddingStrategy;

public class MaxQueryBiddingStrategyJxtaImpl implements IBiddingStrategy {
	// Es soll nur geboten werden, wenn die Anzahl an Anfragen die schon
	// überwacht werden und die Azahl an
	// Gebote die noch offen sind, kleiner 5 sind.
	private static int MAX_QUERYS = 10;

	public boolean bidding(Query q) {
		if (q.getStatus() == Status.OPEN) {

			int i = 0;
			for (String key : AdministrationPeerJxtaImpl.getInstance()
					.getQueries().keySet()) {
				QueryJxtaImpl query = AdministrationPeerJxtaImpl.getInstance()
						.getQueries().get(key);
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
