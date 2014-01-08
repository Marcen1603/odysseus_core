package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.SubPlan;
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
	
	public static Map<String, List<SubPlan>> allocate(ID sharedQueryID, Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(subPlans, transCfg);
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
				Collection<Bid> bids = auction.getBidsFuture().get();

				LOG.info("Got {} bids from remote peers", bids.size());

				// calc own bid
				IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
				Optional<Double> bidValue = bidProvider.calculateBid(Helper.getLogicalQuery(auction.getAuctionAdvertisement().getPqlStatement()).get(0), auction.getAuctionAdvertisement().getTransCfgName());
				
				if( bidValue.isPresent() ) {
					bids.add(new Bid(p2pNetworkManager.getLocalPeerID(), bidValue.get()));
				}
				
				Bid bid = determineBestBid(bids);

				String peerName = bid.getBidderPeerID().toString();
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

				LOG.debug("Peer {} won auction {} with bid {}", new String[] { peerName, auction.getAuctionAdvertisement().getAuctionId().toString(), String.valueOf(bid.getValue())});
			}
		} catch (Exception e) {
			LOG.error("Could not determine the winner of an action", e);
		}
		return winners;
	}
	
	private static Bid determineBestBid(Collection<Bid> bids) {
		Bid bestBid = null;
		for( Bid bid : bids ) {
			if( bestBid == null || bid.getValue() > bestBid.getValue() ) {
				bestBid = bid;
			}
		}
		return bestBid;
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

	private static List<AuctionSummary> publishAuctionsForRemoteSubPlans(Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(subPlans, "SubPlans must not be null!");

		List<AuctionSummary> auctions = Lists.newArrayList();

		int auctionsCount = 0;
		for (SubPlan subPlan : subPlans) {
			final AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());
			adv.setPqlStatement(pqlGenerator.generatePQLStatement(subPlan.getLogicalPlan()));
			adv.setTransCfgName(transCfg.getName());
			adv.setAuctionId(IDFactory.newContentID(p2pNetworkManager.getLocalPeerGroupID(), true));
			adv.setID(IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()));
			adv.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());
			
			Future<Collection<Bid>> bidsFuture = Communicator.getInstance().publishAuction(adv);
			auctions.add(new AuctionSummary(adv, bidsFuture, subPlan));
			
			auctionsCount++;
		}

		LOG.info("Auctions for {} remote sub plans listed ", auctionsCount);

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
