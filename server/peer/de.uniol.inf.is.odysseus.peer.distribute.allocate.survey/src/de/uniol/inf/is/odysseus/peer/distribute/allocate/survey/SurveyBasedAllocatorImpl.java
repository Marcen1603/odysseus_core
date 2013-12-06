package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.CostSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Communicator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.CostCalculator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;

public final class SurveyBasedAllocatorImpl {

	private static final Logger log = LoggerFactory.getLogger(SurveyBasedAllocatorImpl.class);

	private SurveyBasedAllocatorImpl() {
		
	}	
	
	public static Map<String, List<SubPlan>> allocate(ID sharedQueryID, Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(sharedQueryID, subPlans, transCfg);
		return evaluateBids(auctions, subPlans);
	}

	private static Map<String, List<SubPlan>> evaluateBids(List<AuctionSummary> auctions, Collection<SubPlan> subPlans) {
		Map<String, List<SubPlan>> destinationMap = determineWinners(auctions);
		addLocalPlans(destinationMap, subPlans);
		return destinationMap;
	}

	private static Map<String, List<SubPlan>> determineWinners(List<AuctionSummary> auctions) {
		Map<String, List<SubPlan>> winners = Maps.newHashMap();
		try {
			for (AuctionSummary auction : auctions) {
				List<AuctionResponseAdvertisement> bids = auction.getResponses().get();

				log.info("Got {} bids from remote peers", bids.size());

				// calc own bid
				bids.add(wrapInAuctionResponseAdvertisement(auction.getAuction().getAuctionId(), calcOwnBid(auction.getAuction().getPqlStatement(), auction.getAuction().getTransCfgName())));

				Collections.sort(bids);
				AuctionResponseAdvertisement bid = bids.get(bids.size() - 1);

				String peerName = bid.getOwnerPeerId().toString();
				if (peerName.equals(P2PNetworkManagerService.get().getLocalPeerID().toString())) {
					peerName = "local";
				}

				List<SubPlan> list = winners.get(peerName);

				if (list == null) {
					list = Lists.newArrayList();
					winners.put(peerName, list);
				}
				auction.getSubPlan().addDestinationName(peerName);
				list.add(auction.getSubPlan());

				log.debug("Peer {} won auction {} with bid {}", new String[] { peerName, bid.getAuctionId(), bid.getBid() + "" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return winners;
	}
	
	private static AuctionResponseAdvertisement wrapInAuctionResponseAdvertisement(String auctionId, double bid) {
		final AuctionResponseAdvertisement adv = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
		adv.setBid(bid);
		adv.setAuctionId(auctionId);
		adv.setOwnerPeerId(P2PNetworkManagerService.get().getLocalPeerID());
		return adv;
	}
	
	private static void addLocalPlans(Map<String, List<SubPlan>> destinationMap, Collection<SubPlan> subPlans) {
		if (!destinationMap.containsKey("local")) {
			destinationMap.put("local", getLocalPlans(subPlans));
		} else {
			List<SubPlan> localPlans = getLocalPlans(subPlans);
			Set<SubPlan> toAdd = Sets.newHashSet();
			for (SubPlan localPlan : localPlans) {
				boolean alreadyExists = false;
				for (SubPlan subPlan : destinationMap.get("local")) {
					if (localPlan == subPlan) {
						alreadyExists = true;
						break;
					}
				}
				if (!alreadyExists) {
					toAdd.add(localPlan);
				}
			}
			destinationMap.get("local").addAll(toAdd);
		}
	}

	private static List<AuctionSummary> publishAuctionsForRemoteSubPlans(ID sharedQueryID, Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(subPlans, "SubPlans must not be null!");

		List<AuctionSummary> auctions = Lists.newArrayList();

		int auctionsCount = 0;
		for (SubPlan subPlan : subPlans) {

			if (subPlan.hasLocalDestination()) {
				log.info("Sub plan {} will be placed on local host", subPlan);
				continue;
			}
			final AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());
			adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(subPlan.getLogicalPlan()));
			adv.setSharedQueryID(sharedQueryID);
			adv.setTransCfgName(transCfg.getName());
			adv.setAuctionId(UUID.randomUUID().toString());
			auctions.add(new AuctionSummary(adv, Communicator.getInstance().publishAuction(adv), subPlan));
			auctionsCount++;
		}

		log.info("Auctions for {} remote sub plans listed (sharedQueryId: {})", auctionsCount, sharedQueryID);

		return auctions;
	}

	private static List<SubPlan> getLocalPlans(Collection<SubPlan> subPlans) {
		List<SubPlan> localParts = Lists.newArrayList();
		for (SubPlan subPlan : subPlans) {
			if (subPlan.hasLocalDestination()) {
				localParts.add(subPlan);
			}
		}

		return localParts;
	}

	private static double calcOwnBid(String pqlStatement, String transCfgName) {
		ILogicalQuery query = Helper.getLogicalQuery(pqlStatement).get(0);
		CostSummary costs = CostCalculator.calcCostsForPlan(query, transCfgName);

		return CostCalculator.calcBid(query.getLogicalPlan(), costs);
	}
}
