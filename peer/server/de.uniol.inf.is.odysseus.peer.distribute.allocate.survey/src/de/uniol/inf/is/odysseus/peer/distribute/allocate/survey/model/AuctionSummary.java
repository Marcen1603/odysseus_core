package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model;

import java.util.Collection;
import java.util.concurrent.Future;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;

public final class AuctionSummary {
	
	private final AuctionQueryAdvertisement query;
	private final Future<Collection<Bid>> responses;
	private final ILogicalQueryPart queryPart;

	public AuctionSummary(AuctionQueryAdvertisement query, Future<Collection<Bid>> bids, ILogicalQueryPart queryPart) {
		Preconditions.checkNotNull(query, "Auction advertisement must not be null");
		Preconditions.checkNotNull(bids, "Responses future must not be null");
		Preconditions.checkNotNull(queryPart, "Query part of auction must not be null");
		
		this.query = query;
		this.responses = bids;
		this.queryPart = queryPart;
	}

	public AuctionQueryAdvertisement getAuctionAdvertisement() {
		return query;
	}

	public Future<Collection<Bid>> getBidsFuture() {
		return responses;
	}

	public ILogicalQueryPart getLogicalQueryPart() {
		return queryPart;
	}
}