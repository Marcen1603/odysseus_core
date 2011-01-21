package de.uniol.inf.is.odysseus.p2p.administrationpeer.strategy;

import de.uniol.inf.is.odysseus.p2p.queryhandling.P2PQuery;

public interface IThinPeerBiddingStrategy {

	boolean bidding(P2PQuery q);

}
