package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Communicator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyBasedAllocator.class);
	
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

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters)
			throws QueryPartAllocationException {
		// copy --> original. Needed since allocator inserts dummyAOs
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);

		Map<ILogicalQueryPart, PeerID> allocationMap = allocate(queryPartsCopyMap.keySet(), config);
		Map<ILogicalQueryPart, PeerID> allocationMapParts = transformToOriginalLogicalQueryParts(allocationMap, queryPartsCopyMap);

		return allocationMapParts;
	}

	private static Map<ILogicalQueryPart, PeerID> transformToOriginalLogicalQueryParts(Map<ILogicalQueryPart, PeerID> allocationMapPeerID, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<ILogicalQueryPart, PeerID> partMap = Maps.newHashMap();

		for (ILogicalQueryPart queryPartCopy : allocationMapPeerID.keySet()) {
			ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(queryPartCopy);
			partMap.put(originalQueryPart, allocationMapPeerID.get(queryPartCopy));
		}

		return partMap;
	}

	private static Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration transCfg) throws QueryPartAllocationException {
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(queryParts, transCfg);
		Map<ILogicalQueryPart, PeerID> destinationMap = determinePeers(auctions);
		return destinationMap;
	}

	private static List<AuctionSummary> publishAuctionsForRemoteSubPlans(Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(queryParts, "Collection of query parts must not be null!");

		List<AuctionSummary> auctions = Lists.newArrayList();

		Helper.insertDummyAOs(queryParts);
		for (ILogicalQueryPart queryPart : queryParts) {
			AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());

			adv.setPqlStatement(pqlGenerator.generatePQLStatement(queryPart.getOperators().iterator().next()));
			adv.setTransCfgName(transCfg.getName());
			adv.setAuctionId(IDFactory.newContentID(p2pNetworkManager.getLocalPeerGroupID(), true));
			adv.setID(IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()));
			adv.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());

			Future<Collection<Bid>> bidsFuture = Communicator.getInstance().publishAuction(adv);
			auctions.add(new AuctionSummary(adv, bidsFuture, queryPart));
		}

		LOG.info("Auctions for {} remote sub plans listed ", auctions.size());

		return auctions;
	}

	private static Map<ILogicalQueryPart, PeerID> determinePeers(List<AuctionSummary> auctions) throws QueryPartAllocationException {
		try {
			Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = determineBidPlanMap(auctions);
			return determineBestPeers(bidPlanMap);
		} catch (QueryPartAllocationException e) {
			throw e;
		} catch (Exception e) {
			throw new QueryPartAllocationException("Could not determine the winner of an auction", e);
		}
	}

	private static Map<ILogicalQueryPart, Collection<Bid>> determineBidPlanMap(List<AuctionSummary> auctions) throws InterruptedException, ExecutionException, QueryPartAllocationException {
		Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = Maps.newHashMap();

		IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
		PeerID localPeerID = p2pNetworkManager.getLocalPeerID();

		for (AuctionSummary auction : auctions) {
			Collection<Bid> bids = auction.getBidsFuture().get();

			Optional<Double> bidValue = bidProvider.calculateBid(Helper.getLogicalQuery(auction.getAuctionAdvertisement().getPqlStatement()).get(0), auction.getAuctionAdvertisement().getTransCfgName());
			if (bidValue.isPresent()) {
				bids.add(new Bid(localPeerID, bidValue.get()));
			}

			if (bids.isEmpty()) {
				throw new QueryPartAllocationException("Could not allocate a subplan since there are no bids for");
			}

			bidPlanMap.put(auction.getLogicalQueryPart(), bids);
		}

		return bidPlanMap;
	}

	private static Map<ILogicalQueryPart, PeerID> determineBestPeers(Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap) {
		Map<ILogicalQueryPart, PeerID> bestBids = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : bidPlanMap.keySet()) {
			bestBids.put(queryPart, determineBestBid(bidPlanMap.get(queryPart)).getBidderPeerID());
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
}
