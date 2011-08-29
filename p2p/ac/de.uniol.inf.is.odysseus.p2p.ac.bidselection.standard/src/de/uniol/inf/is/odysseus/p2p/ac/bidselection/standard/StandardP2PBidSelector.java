package de.uniol.inf.is.odysseus.p2p.ac.bidselection.standard;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.ac.bidselection.IP2PBidSelector;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;

public class StandardP2PBidSelector implements IP2PBidSelector {

	@Override
	public Bid selectBid(List<Bid> bids) {
		// 1. Are there positiv bids?
		List<Bid> validBids = new ArrayList<Bid>();
		for (Bid b : bids) {
			Double bidValue = Double.valueOf(b.getBid());
			
			if (bidValue >= 0.0) {
				validBids.add(b);
			}
		}

		if (!validBids.isEmpty()) {
			double lowestBidValue = Double.MAX_VALUE;
			Bid lowestBid = null;
			for( Bid b : validBids ) {
				Double val = Double.valueOf(b.getBid());
				if( val < lowestBidValue ) {
					lowestBidValue = val;
					lowestBid = b;
				}
			}
			
			return lowestBid;
			
		} else {
			return null;
		}
	}

}
