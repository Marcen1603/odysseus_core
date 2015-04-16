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
import com.google.common.collect.Maps.EntryTransformer;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.costmodel.DetailCost;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.force.ForceModel;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Communicator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.graph.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.network.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyBasedAllocator.class);

	private static IP2PNetworkManager p2pNetworkManager;
	private static IPeerDictionary peerDictionary;
	private static IPingMap pingMap;

	private Map<ILogicalQueryPart, List<PeerID>> allocationMapResult;

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
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	// called by OSGi-DS
	public static void bindPingMap(IPingMap serv) {
		pingMap = serv;
	}

	// called by OSGi-DS
	public static void unbindPingMap(IPingMap serv) {
		if (pingMap == serv) {
			pingMap = null;
		}
	}

	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);
		List<ILogicalQueryPart> queryPartsToSurvey = Lists.newArrayList(queryPartsCopyMap.keySet());

		int latencyWeight = determineLatencyWeight(allocatorParameters);
		int bidWeight = determineBidWeight(allocatorParameters);
		boolean isOwnBidUsed = isOwnBidUsed(allocatorParameters);

		Map<ILogicalQueryPart, List<PeerID>> allocationMap = allocate(queryPartsToSurvey, config, isOwnBidUsed, latencyWeight, bidWeight);
		allocationMapResult = transformToOriginalLogicalQueryParts(allocationMap, queryPartsCopyMap);

		return determineAllocationResult(allocationMapResult);
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(Map<ILogicalQueryPart, PeerID> previousAllocationMap, Collection<PeerID> faultyPeers, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		for (ILogicalQueryPart queryPart : previousAllocationMap.keySet().toArray(new ILogicalQueryPart[0])) {
			PeerID allocatedPeer = previousAllocationMap.get(queryPart);
			if (faultyPeers.contains(allocatedPeer)) {
				Optional<PeerID> nextBestPeerID = determineNextBestPeerID(allocationMapResult.get(queryPart), faultyPeers);
				if (nextBestPeerID.isPresent()) {
					previousAllocationMap.put(queryPart, nextBestPeerID.get());
				} else {
					throw new QueryPartAllocationException("Could not reallocate query part " + queryPart + " since there are no bids left");
				}
			}
		}
		return previousAllocationMap;
	}

	private static Optional<PeerID> determineNextBestPeerID(List<PeerID> list, Collection<PeerID> faultyPeers) {
		for (PeerID pid : list) {
			if (!faultyPeers.contains(pid)) {
				return Optional.of(pid);
			}
		}
		return Optional.absent();
	}

	private static Map<ILogicalQueryPart, PeerID> determineAllocationResult(Map<ILogicalQueryPart, List<PeerID>> allocationMapParts) {
		Map<ILogicalQueryPart, PeerID> result = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : allocationMapParts.keySet()) {
			result.put(queryPart, allocationMapParts.get(queryPart).get(0));
		}
		return result;
	}

	private static int determineLatencyWeight(List<String> allocatorParameters) {
		if (allocatorParameters.size() >= 1) {
			try {
				return Integer.valueOf(allocatorParameters.get(0));
			} catch (Throwable t) {
			}
		}
		return 1;
	}

	private static int determineBidWeight(List<String> allocatorParameters) {
		if (allocatorParameters.size() >= 2) {
			try {
				return Integer.valueOf(allocatorParameters.get(1));
			} catch (Throwable t) {
			}
		}
		return 1;
	}

	private static boolean isOwnBidUsed(List<String> allocatorParameters) {
		return allocatorParameters.size() < 3 || !allocatorParameters.get(2).equalsIgnoreCase("notlocal");
	}

	private static Map<ILogicalQueryPart, List<PeerID>> transformToOriginalLogicalQueryParts(Map<ILogicalQueryPart, List<PeerID>> allocationMapPeerID, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<ILogicalQueryPart, List<PeerID>> partMap = Maps.newHashMap();

		for (ILogicalQueryPart queryPartCopy : allocationMapPeerID.keySet()) {
			ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(queryPartCopy);
			partMap.put(originalQueryPart, allocationMapPeerID.get(queryPartCopy));
		}

		return partMap;
	}

	private static Map<ILogicalQueryPart, List<PeerID>> allocate(Collection<ILogicalQueryPart> queryParts, QueryBuildConfiguration transCfg, boolean ownBid, int latencyWeight, int bidWeight) throws QueryPartAllocationException {
		QueryPartGraph partGraph = new QueryPartGraph(queryParts);

		Collection<ILogicalOperator> operators = Lists.newArrayList();
		for (ILogicalQueryPart queryPart : queryParts) {
			operators.addAll(queryPart.getOperators());
		}

		Map<ILogicalOperator, DetailCost> logicalOperatorEstimationMap = Helper.determineOperatorCostEstimations(operators);

		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(queryParts, logicalOperatorEstimationMap, transCfg);
		Map<ILogicalQueryPart, List<PeerID>> destinationMap = determinePeerAssignment(auctions, partGraph, ownBid, latencyWeight, bidWeight, logicalOperatorEstimationMap);
		return destinationMap;
	}

	private static List<AuctionSummary> publishAuctionsForRemoteSubPlans(Collection<ILogicalQueryPart> queryParts, Map<ILogicalOperator, DetailCost> logicalOperatorEstimationMap, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(queryParts, "Collection of query parts must not be null!");

		List<AuctionSummary> auctions = Lists.newArrayList();

		Helper.insertDummyAOs(queryParts, logicalOperatorEstimationMap);
		LOG.debug("Generating {} auctions", queryParts.size());
		for (ILogicalQueryPart queryPart : queryParts) {
			AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());

			LOG.debug("Generating auction...");
			adv.setPqlStatement(LogicalQueryHelper.generatePQLStatementFromQueryPart(queryPart));
			LOG.debug("PQL statement is\n{}\n\n", adv.getPqlStatement());
			adv.setTransCfgName(transCfg.getName());
			adv.setAuctionId(IDFactory.newContentID(p2pNetworkManager.getLocalPeerGroupID(), true));
			adv.setID(IDFactory.newPipeID(p2pNetworkManager.getLocalPeerGroupID()));
			adv.setOwnerPeerId(p2pNetworkManager.getLocalPeerID());

			Future<Collection<Bid>> bidsFuture = Communicator.getInstance().publishAuction(adv);
			auctions.add(new AuctionSummary(adv, bidsFuture, queryPart));
		}

		LOG.debug("Auctions for {} remote sub plans listed ", auctions.size());

		return auctions;
	}

	private static Map<ILogicalQueryPart, List<PeerID>> determinePeerAssignment(List<AuctionSummary> auctions, QueryPartGraph partGraph, boolean ownBid, int latencyWeight, int bidWeight, Map<ILogicalOperator, DetailCost> logicalOperatorEstimationMap) throws QueryPartAllocationException {
		try {
			Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = determineBidPlanMap(auctions, ownBid);

			Map<ILogicalOperator, Double> dataRateMap = createDataRateMap(logicalOperatorEstimationMap);
			ForceModel model = new ForceModel(bidPlanMap, partGraph, dataRateMap, bidWeight, latencyWeight);
			model.run();

			return model.getResult();
		} catch (QueryPartAllocationException e) {
			throw e;
		} catch (Exception e) {
			throw new QueryPartAllocationException("Could not determine the winner of an auction", e);
		}
	}

	private static Map<ILogicalOperator, Double> createDataRateMap(Map<ILogicalOperator, DetailCost> logicalOperatorEstimationMap) {
		return Maps.transformEntries(logicalOperatorEstimationMap, new EntryTransformer<ILogicalOperator, DetailCost, Double>() {
			@Override
			public Double transformEntry(ILogicalOperator key, DetailCost value) {
				return value.getDatarate();
			}
		});
	}

	private static Map<ILogicalQueryPart, Collection<Bid>> determineBidPlanMap(List<AuctionSummary> auctions, boolean ownBid) throws InterruptedException, ExecutionException, QueryPartAllocationException {
		Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = Maps.newHashMap();

		IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
		PeerID localPeerID = p2pNetworkManager.getLocalPeerID();

		for (AuctionSummary auction : auctions) {
			Collection<Bid> bids = auction.getBidsFuture().get();

			if (ownBid || bids.isEmpty()) {
				Optional<Double> bidValue;
				if (ownBid) {
					LOG.debug("Generating own bid because we can: {}", auction.getLogicalQueryPart());
					bidValue = bidProvider.calculateBid(Helper.getLogicalQuery(auction.getAuctionAdvertisement().getPqlStatement()).get(0), auction.getAuctionAdvertisement().getTransCfgName());
				} else {
					LOG.debug("Generating own bid since there was no bids for: {}", auction.getLogicalQueryPart());
					bidValue = Optional.of(1.0); // do not need to check cost
													// model... we are the only
													// one here
				}
				if (bidValue.isPresent()) {
					bids.add(new Bid(localPeerID, bidValue.get()));
				}
			}

			if (bids.isEmpty()) {
				throw new QueryPartAllocationException("Could not allocate a subplan since there are no bids for");
			}

			bidPlanMap.put(auction.getLogicalQueryPart(), bids);
		}

		if (LOG.isDebugEnabled()) {
			printBidPlan(bidPlanMap);
		}

		return bidPlanMap;
	}

	private static void printBidPlan(Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap) {
		LOG.debug("Following bids received");
		for (ILogicalQueryPart queryPart : bidPlanMap.keySet()) {
			LOG.debug("\t{}:", queryPart);
			for (Bid bid : bidPlanMap.get(queryPart)) {
				LOG.debug("\t\t{}:\t{}", peerDictionary.getRemotePeerName(bid.getBidderPeerID()), bid.getValue());
			}
		}
	}
}
