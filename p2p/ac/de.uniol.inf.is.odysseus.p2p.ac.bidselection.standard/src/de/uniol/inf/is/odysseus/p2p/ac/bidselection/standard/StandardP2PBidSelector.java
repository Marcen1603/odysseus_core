package de.uniol.inf.is.odysseus.p2p.ac.bidselection.standard;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.p2p.ac.bidselection.IP2PBidSelector;
import de.uniol.inf.is.odysseus.p2p.queryhandling.Bid;

/**
 * Standardimplementierung der Schnittstelle {@link IP2PBidSelector}. Es wird
 * das Gebot mit dem niedrigsten Gebot ausgewählt. Dieser Selector sollte nur im
 * Zusammenhang mit StandardP2PGenerator verwendet werden.
 * 
 * @author Timo Michelsen
 * 
 */
public class StandardP2PBidSelector implements IP2PBidSelector {

	@Override
	public Bid selectBid(List<Bid> bids) {
		// 1. Are there positiv bids?
		List<Bid> validBids = new ArrayList<Bid>();
		for (Bid b : bids) {
			int bidValue = b.getBidValue();

			if (bidValue >= 0) {
				validBids.add(b);
			}
		}

		if (!validBids.isEmpty()) {
			int lowestBidValue = Integer.MAX_VALUE;
			Bid lowestBid = null;
			for (Bid b : validBids) {
				int val = b.getBidValue();
				if (val < lowestBidValue) {
					lowestBidValue = val;
					lowestBid = b;
				}
			}

			return lowestBid;

		}
        return null;
	}

}
