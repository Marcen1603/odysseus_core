package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
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

	public static Map<SubPlan, PeerID> allocate(Collection<SubPlan> subPlans, QueryBuildConfiguration transCfg) throws QueryPartAllocationException {
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(subPlans, transCfg);
		Map<SubPlan, PeerID> destinationMap = determineWinners(auctions);
		return destinationMap;
	}

	private static Map<SubPlan, PeerID> determineWinners(List<AuctionSummary> auctions) throws QueryPartAllocationException {
		try {
			Map<SubPlan, Collection<Bid>> bidPlanMap = determineBidPlanMap(auctions);
			Map<SubPlan, Bid> bestBidPerSubPlan = determineBestBids(bidPlanMap);

			return determinePeersWithBestBid( bestBidPerSubPlan );
		} catch( QueryPartAllocationException e ) {
			throw e;
		} catch (Exception e) {
			throw new QueryPartAllocationException("Could not determine the winner of an auction", e);
		}
	}

	private static Map<SubPlan, Collection<Bid>> determineBidPlanMap(List<AuctionSummary> auctions) throws InterruptedException, ExecutionException, QueryPartAllocationException {
		Map<SubPlan, Collection<Bid>> bidPlanMap = Maps.newHashMap();
		
		IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
		PeerID localPeerID = p2pNetworkManager.getLocalPeerID();

		for (AuctionSummary auction : auctions) {
			Collection<Bid> bids = auction.getBidsFuture().get();
			
			Optional<Double> bidValue = bidProvider.calculateBid(Helper.getLogicalQuery(auction.getAuctionAdvertisement().getPqlStatement()).get(0), auction.getAuctionAdvertisement().getTransCfgName());
			if (bidValue.isPresent()) {
				bids.add(new Bid(localPeerID, bidValue.get()));
			}

			if( bids.isEmpty() ) {
				throw new QueryPartAllocationException("Could not allocate a subplan since there are no bids for");
			}
			
			bidPlanMap.put(auction.getSubPlan(), bids);
		}

		return bidPlanMap;
	}

	private static Map<SubPlan, Bid> determineBestBids(Map<SubPlan, Collection<Bid>> bidPlanMap) {
		Map<SubPlan, Bid> bestBids = Maps.newHashMap();
		for (SubPlan subPlan : bidPlanMap.keySet()) {
			bestBids.put(subPlan, determineBestBid(bidPlanMap.get(subPlan)));
		}
		return bestBids;
	}

	private static Bid determineBestBid(Collection<Bid> bids) {
		Bid bestBid = null;
		for (Bid bid : bids) {
			if (bestBid == null || bid.getValue() > bestBid.getValue()) {
				bestBid = bid;
			}
		}
		return bestBid;
	}

	private static Map<SubPlan, PeerID> determinePeersWithBestBid(Map<SubPlan, Bid> bestBidPerSubPlan) {
		Map<SubPlan, PeerID> bestPeers = Maps.newHashMap();
		for( SubPlan subPlan : bestBidPerSubPlan.keySet() ) {
			bestPeers.put(subPlan, bestBidPerSubPlan.get(subPlan).getBidderPeerID());
		}
		return bestPeers;
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
}
