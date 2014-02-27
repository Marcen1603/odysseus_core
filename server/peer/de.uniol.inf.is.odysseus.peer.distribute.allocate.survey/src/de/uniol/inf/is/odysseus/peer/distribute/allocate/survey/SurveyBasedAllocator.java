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
import de.uniol.inf.is.odysseus.costmodel.operator.OperatorEstimation;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
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
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyBasedAllocator.class);

	private static IP2PNetworkManager p2pNetworkManager;
	private static IP2PDictionary p2pDictionary;
	private static IPingMap pingMap;

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
	public static void bindP2PDictionary(IP2PDictionary serv) {
		p2pDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PDictionary(IP2PDictionary serv) {
		if (p2pDictionary == serv) {
			p2pDictionary = null;
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
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters)
			throws QueryPartAllocationException {
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);
		List<ILogicalQueryPart> queryPartsToSurvey = Lists.newArrayList(queryPartsCopyMap.keySet());

		Map<ILogicalQueryPart, PeerID> allocationMap = allocate(queryPartsToSurvey, query, config, isOwnBidUsed(allocatorParameters));
		Map<ILogicalQueryPart, PeerID> allocationMapParts = transformToOriginalLogicalQueryParts(allocationMap, queryPartsCopyMap);

		return allocationMapParts;
	}

	private static boolean isOwnBidUsed(List<String> allocatorParameters) {
		return allocatorParameters.isEmpty() || !allocatorParameters.get(0).equalsIgnoreCase("notlocal");
	}

	private static Map<ILogicalQueryPart, PeerID> transformToOriginalLogicalQueryParts(Map<ILogicalQueryPart, PeerID> allocationMapPeerID, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<ILogicalQueryPart, PeerID> partMap = Maps.newHashMap();

		for (ILogicalQueryPart queryPartCopy : allocationMapPeerID.keySet()) {
			ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(queryPartCopy);
			partMap.put(originalQueryPart, allocationMapPeerID.get(queryPartCopy));
		}

		return partMap;
	}

	private static Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, QueryBuildConfiguration transCfg, boolean ownBid) throws QueryPartAllocationException {
		QueryPartGraph partGraph = new QueryPartGraph(queryParts);
		
		Collection<ILogicalOperator> operators = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : queryParts ) {
			operators.addAll(queryPart.getOperators());
		}
		
		Map<ILogicalOperator, OperatorEstimation<?>> logicalOperatorEstimationMap = Helper.determineOperatorCostEstimations(operators, transCfg.getName());
		
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(queryParts, logicalOperatorEstimationMap, transCfg);
		Map<ILogicalQueryPart, PeerID> destinationMap = determinePeerAssignment(auctions, partGraph, ownBid, logicalOperatorEstimationMap);
		return destinationMap;
	}

	private static List<AuctionSummary> publishAuctionsForRemoteSubPlans(Collection<ILogicalQueryPart> queryParts, Map<ILogicalOperator, OperatorEstimation<?>> logicalOperatorEstimationMap, QueryBuildConfiguration transCfg) {
		Preconditions.checkNotNull(queryParts, "Collection of query parts must not be null!");

		List<AuctionSummary> auctions = Lists.newArrayList();

		Helper.insertDummyAOs(queryParts, logicalOperatorEstimationMap);
		for (ILogicalQueryPart queryPart : queryParts) {
			AuctionQueryAdvertisement adv = (AuctionQueryAdvertisement) AdvertisementFactory.newAdvertisement(AuctionQueryAdvertisement.getAdvertisementType());

			adv.setPqlStatement(LogicalQueryHelper.generatePQLStatementFromQueryPart(queryPart));
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

	private static Map<ILogicalQueryPart, PeerID> determinePeerAssignment(List<AuctionSummary> auctions, QueryPartGraph partGraph, boolean ownBid, Map<ILogicalOperator, OperatorEstimation<?>> logicalOperatorEstimationMap) throws QueryPartAllocationException {
		try {
			Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = determineBidPlanMap(auctions, ownBid);

			Map<ILogicalOperator, Double> dataRateMap = createDataRateMap(logicalOperatorEstimationMap);
			ForceModel model = new ForceModel(bidPlanMap, partGraph, dataRateMap);
			model.run();
			
			return model.getResult();
		} catch (QueryPartAllocationException e) {
			throw e;
		} catch (Exception e) {
			throw new QueryPartAllocationException("Could not determine the winner of an auction", e);
		}
	}

	private static Map<ILogicalOperator, Double> createDataRateMap(Map<ILogicalOperator, OperatorEstimation<?>> logicalOperatorEstimationMap) {
		return Maps.transformEntries(logicalOperatorEstimationMap, new EntryTransformer<ILogicalOperator, OperatorEstimation<?>, Double>() {
			@Override
			public Double transformEntry(ILogicalOperator key, OperatorEstimation<?> value) {
				return value.getDataStream().getDataRate();
			}
		});
	}

	private static Map<ILogicalQueryPart, Collection<Bid>> determineBidPlanMap(List<AuctionSummary> auctions, boolean ownBid) throws InterruptedException, ExecutionException, QueryPartAllocationException {
		Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = Maps.newHashMap();

		IBidProvider bidProvider = SurveyBasedAllocationPlugIn.getSelectedBidProvider();
		PeerID localPeerID = p2pNetworkManager.getLocalPeerID();

		for (AuctionSummary auction : auctions) {
			Collection<Bid> bids = auction.getBidsFuture().get();

			if( ownBid || bids.isEmpty()) {
				if( ownBid ) {
					LOG.debug("Generating own bid because we can: {}", auction.getLogicalQueryPart());
				} else {
					LOG.debug("Generating own bid since there was no bids for: {}", auction.getLogicalQueryPart());
				}
				Optional<Double> bidValue = bidProvider.calculateBid(Helper.getLogicalQuery(auction.getAuctionAdvertisement().getPqlStatement()).get(0), auction.getAuctionAdvertisement().getTransCfgName());
				if (bidValue.isPresent()) {
					bids.add(new Bid(localPeerID, bidValue.get()));
				}
			}

			if (bids.isEmpty() ) {
				throw new QueryPartAllocationException("Could not allocate a subplan since there are no bids for");
			}

			bidPlanMap.put(auction.getLogicalQueryPart(), bids);
		}
		
		if( LOG.isDebugEnabled() ) {
			printBidPlan(bidPlanMap);
		}

		return bidPlanMap;
	}

	private static void printBidPlan(Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap) {
		LOG.debug("Following bids received");
		for( ILogicalQueryPart queryPart : bidPlanMap.keySet() ) {
			LOG.debug("\t{}:", queryPart);
			for( Bid bid : bidPlanMap.get(queryPart)) {
				LOG.debug("\t\t{}:\t{}", p2pDictionary.getRemotePeerName(bid.getBidderPeerID()).get(), bid.getValue());
			}
		}
	}
}
