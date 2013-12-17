package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model;

import java.util.Collection;
import java.util.concurrent.Future;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;

public final class AuctionSummary {
	
	private final AuctionQueryAdvertisement query;
	private final Future<Collection<AuctionResponseAdvertisement>> responses;
	private final SubPlan subPlan;

	public AuctionSummary(AuctionQueryAdvertisement query, Future<Collection<AuctionResponseAdvertisement>> responses, SubPlan subPlan) {
		Preconditions.checkNotNull(query, "Auction advertisement must not be null");
		Preconditions.checkNotNull(responses, "Responses future must not be null");
		Preconditions.checkNotNull(subPlan, "Subplan of auction must not be null");
		
		this.query = query;
		this.responses = responses;
		this.subPlan = subPlan;
	}

	public AuctionQueryAdvertisement getAuctionAdvertisement() {
		return query;
	}

	public Future<Collection<AuctionResponseAdvertisement>> getResponsesFuture() {
		return responses;
	}

	public SubPlan getSubPlan() {
		return subPlan;
	}
}