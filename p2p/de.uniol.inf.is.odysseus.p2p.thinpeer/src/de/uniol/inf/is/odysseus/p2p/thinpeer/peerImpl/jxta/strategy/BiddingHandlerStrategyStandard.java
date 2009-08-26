package de.uniol.inf.is.odysseus.p2p.thinpeer.peerImpl.jxta.strategy;

import java.util.ArrayList;
import java.util.Collections;

import de.uniol.inf.is.odysseus.p2p.thinpeer.strategy.IBiddingHandlerStrategy;
import de.uniol.inf.is.odysseus.p2p.utils.jxta.Bid;

public class BiddingHandlerStrategyStandard implements IBiddingHandlerStrategy {

	@SuppressWarnings("unchecked")
	@Override
	public Object handleBiddings(Object _biddings) {
		ArrayList<Bid> biddings = (ArrayList<Bid>) _biddings;
		Collections.shuffle(biddings);
		return biddings.get(0);
	}
}
