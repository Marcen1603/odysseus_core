package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionResponseAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.BidCalculator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Communicator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;

public final class SurveyBasedAllocatorImpl {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyBasedAllocatorImpl.class);
	
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPQLGenerator pqlGenerator;

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}

	// called by OSGi-DS
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
		}
	}
	
	public static Map<String, List<SubPlan>> allocate(ID auctionID, Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(auctionID, subPlans, transCfg);
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
				Collection<AuctionResponseAdvertisement> bids = auction.getResponsesFuture().get();

				LOG.info("Got {} bids from remote peers", bids.size());

				// calc own bid
				double bidValue = BidCalculator.calcBid(Helper.getLogicalQuery(auction.getAuctionAdvertisement().getPqlStatement()).get(0), auction.getAuctionAdvertisement().getTransCfgName());
				bids.add(wrapInAuctionResponseAdvertisement(auction.getAuctionAdvertisement().getAuctionId(), bidValue));

				AuctionResponseAdvertisement bid = determineBestBid(bids);

				String peerName = bid.getOwnerPeerId().toString();
				if (peerName.equals(p2pNetworkManager.getLocalPeerID().toString())) {
					peerName = "local";
				}

				List<SubPlan> list = winners.get(peerName);

				if (list == null) {
					list = Lists.newArrayList();
					winners.put(peerName, list);
				}
				auction.getSubPlan().addDestinationName(peerName);
				list.add(auction.getSubPlan());

				LOG.debug("Peer {} won auction {} with bid {}", new String[] { peerName, bid.getAuctionId(), bid.getBid() + "" });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return winners;
	}
	
	private static AuctionResponseAdvertisement determineBestBid(Collection<AuctionResponseAdvertisement> bids) {
		AuctionResponseAdvertisement bestBid = null;
		for( AuctionResponseAdvertisement bid : bids ) {
			if( bestBid == null || bid.getBid() > bestBid.getBid() ) {
				bestBid = bid;
			}
		}
		return bestBid;
	}

	private static AuctionResponseAdvertisement wrapInAuctionResponseAdvertisement(String auctionId, double bid) {
		final AuctionResponseAdvertisement adv = (AuctionResponseAdvertisement) AdvertisementFactory.newAdvertisement(AuctionResponseAdvertisement.getAdvertisementType());
		adv.setBid(bid);
		adv.setAuctionId(auctionId);
		adv.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
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

	private static List<AuctionSummary> publishAuctionsForRemoteSubPlans(ID auctionID, Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(subPlans, "SubPlans must not be null!");

		List<AuctionSummary> auctions = Lists.newArrayList();

		int auctionsCount = 0;
		for (SubPlan subPlan : subPlans) {
			final AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());
			adv.setPqlStatement(pqlGenerator.generatePQLStatement(subPlan.getLogicalPlan()));
			adv.setTransCfgName(transCfg.getName());
			adv.setAuctionId(auctionID.toString());
			auctions.add(new AuctionSummary(adv, Communicator.getInstance().publishAuction(adv), subPlan));
			auctionsCount++;
		}

		LOG.info("Auctions for {} remote sub plans listed (sharedQueryId: {})", auctionsCount, auctionID);

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
}
