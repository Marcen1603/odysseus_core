package de.uniol.inf.is.odysseus.p2p.ac.bidselection;

import java.util.List;

import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;

public interface IP2PBidSelector {

	public Bid selectBid(List<Bid> bids);
	
}
