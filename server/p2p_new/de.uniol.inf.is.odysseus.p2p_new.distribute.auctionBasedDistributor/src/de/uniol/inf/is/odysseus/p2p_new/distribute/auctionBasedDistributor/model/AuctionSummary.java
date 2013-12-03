package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model;

import java.util.List;
import java.util.concurrent.Future;

import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement.AuctionResponseAdvertisement;

public class AuctionSummary {
	private final AuctionQueryAdvertisement query;
	private final Future<List<AuctionResponseAdvertisement>> responses;
	private final SubPlan subPlan;
	
	public AuctionSummary(AuctionQueryAdvertisement query,
			Future<List<AuctionResponseAdvertisement>> responses,
			SubPlan subPlan) {
		super();
		this.query = query;
		this.responses = responses;
		this.subPlan = subPlan;
	}
	public AuctionQueryAdvertisement getAuction() {
		return query;
	}
	public Future<List<AuctionResponseAdvertisement>> getResponses() {
		return responses;
	}
	public SubPlan getSubPlan() {
		return subPlan;
	}
}