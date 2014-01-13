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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.LogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.advertisement.AuctionQueryAdvertisement;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.Bid;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.bid.IBidProvider;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.model.AuctionSummary;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Communicator;
import de.uniol.inf.is.odysseus.peer.distribute.allocate.survey.util.Helper;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;
import de.uniol.inf.is.odysseus.peer.distribute.util.QueryPartGraph;
import de.uniol.inf.is.odysseus.peer.ping.IPingMap;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(SurveyBasedAllocator.class);
//	private static final Random RAND = new Random();
	
	private static IP2PNetworkManager p2pNetworkManager;
	private static IPQLGenerator pqlGenerator;
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
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi-DS
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if (pqlGenerator == serv) {
			pqlGenerator = null;
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
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters)
			throws QueryPartAllocationException {
		
		List<ILogicalQueryPart> newQueryParts = createQueryPartsWithSinkQueryPart(queryParts);
		
		// copy --> original. Needed since allocator inserts dummyAOs
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(newQueryParts);
		ILogicalQueryPart realSinksQueryPart = newQueryParts.get(newQueryParts.size() - 1); // see createSinkQueryPart-method
		ILogicalQueryPart realSinksQueryPartCopy = getCopy(realSinksQueryPart, queryPartsCopyMap);

		// survey every query part except the one with the real sinks since this must be placed locally
		List<ILogicalQueryPart> queryPartsToSurvey = Lists.newArrayList(queryPartsCopyMap.keySet());
		queryPartsToSurvey.remove(realSinksQueryPartCopy); 
		
		Map<ILogicalQueryPart, PeerID> allocationMap = allocate(queryPartsToSurvey, config);
		Map<ILogicalQueryPart, PeerID> allocationMapParts = transformToOriginalLogicalQueryParts(allocationMap, queryPartsCopyMap);

		allocationMapParts.put(realSinksQueryPart, p2pNetworkManager.getLocalPeerID());
		return allocationMapParts;
	}

	private static ILogicalQueryPart getCopy(ILogicalQueryPart originalQueryPart, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		for( ILogicalQueryPart copyQueryPart : queryPartsCopyMap.keySet() ) {
			if( queryPartsCopyMap.get(copyQueryPart).equals(originalQueryPart)) {
				return copyQueryPart;
			}
		}
		throw new RuntimeException("Could not find copy of query part " + originalQueryPart);
	}

	private static List<ILogicalQueryPart> createQueryPartsWithSinkQueryPart(Collection<ILogicalQueryPart> queryParts) {
		LOG.debug("Creating sink query part");
		List<ILogicalQueryPart> result = Lists.newArrayList();
		
		Collection<ILogicalOperator> realSinks = Lists.newArrayList();
		for( ILogicalQueryPart queryPart : queryParts ) {
			LOG.debug("Check query part {}", queryPart);
			
			Collection<ILogicalOperator> sinks = LogicalQueryHelper.getSinks(queryPart.getOperators());
			Collection<ILogicalOperator> realSinksOfQueryPart = determineRealSinks(sinks);
			
			Collection<ILogicalOperator> nonRealSinksOfQueryPart = Lists.newArrayList(sinks);
			nonRealSinksOfQueryPart.removeAll(realSinksOfQueryPart);
			
			if( !nonRealSinksOfQueryPart.isEmpty() ) {
				LOG.debug("Found non real sinks {}", nonRealSinksOfQueryPart);
				for( ILogicalOperator nonRealSink : nonRealSinksOfQueryPart ) {
					RenameAO renameAO = new RenameAO();
					renameAO.setNoOp(true);
					renameAO.setOutputSchema(nonRealSink.getOutputSchema());
						
					nonRealSink.subscribeSink(renameAO, 0, 0, nonRealSink.getOutputSchema());
					
					realSinks.add(renameAO);
				}
			}
			realSinks.addAll(realSinksOfQueryPart);
			
			if( !realSinksOfQueryPart.isEmpty() ) {
				LOG.debug("Found real sinks {}", realSinksOfQueryPart);
				
				Collection<ILogicalOperator> allOperatorsOfQueryPart = Lists.newArrayList(queryPart.getOperators());
				allOperatorsOfQueryPart.removeAll(realSinksOfQueryPart);
				
				ILogicalQueryPart newQueryPart = new LogicalQueryPart(allOperatorsOfQueryPart);
				result.add(newQueryPart);
				LOG.debug("Created new query part {}", newQueryPart);
			} else {
				result.add(queryPart);
				LOG.debug("Can use old query part without changes");
			}
		}
		ILogicalQueryPart realSinksQueryPart = new LogicalQueryPart(realSinks);
		result.add(realSinksQueryPart);
		LOG.debug("Created query part with all real sinks {}", realSinksQueryPart);
		
		return result;
	}

	private static Collection<ILogicalOperator> determineRealSinks(Collection<ILogicalOperator> sinks) {
		Collection<ILogicalOperator> realSinks = Lists.newArrayList();
		for( ILogicalOperator sink : sinks ) {
			if( sink.isSinkOperator() && !sink.isSourceOperator() ) {
				realSinks.add(sink);
			}
		}
		return realSinks;
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
		QueryPartGraph partGraph = new QueryPartGraph(queryParts);
		List<AuctionSummary> auctions = publishAuctionsForRemoteSubPlans(queryParts, transCfg);
		Map<ILogicalQueryPart, PeerID> destinationMap = determinePeerAssignment(auctions, partGraph);
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

	private static Map<ILogicalQueryPart, PeerID> determinePeerAssignment(List<AuctionSummary> auctions, QueryPartGraph partGraph) throws QueryPartAllocationException {
		try {
			Map<ILogicalQueryPart, Collection<Bid>> bidPlanMap = determineBidPlanMap(auctions);
			Map<ILogicalQueryPart, PeerID> bestPeers = determineBestPeers(bidPlanMap);
			
//			Map<ILogicalQueryPart, ForceNode> forceNodes = createForceNodes(bestPeers);
			
			
			return bestPeers;
			
		} catch (QueryPartAllocationException e) {
			throw e;
		} catch (Exception e) {
			throw new QueryPartAllocationException("Could not determine the winner of an auction", e);
		}
	}

//	private static Map<ILogicalQueryPart, ForceNode> createForceNodes(Map<ILogicalQueryPart, PeerID> bestPeers) {
//		Map<ILogicalQueryPart, ForceNode> forceNodes = Maps.newHashMap();
//		for( ILogicalQueryPart queryPart : bestPeers.keySet() ) {
//			Optional<IPingMapNode> optPingMapNode = pingMap.getNode(bestPeers.get(queryPart));
//			Vector3D nodePosition = optPingMapNode.isPresent() ? optPingMapNode.get().getPosition() : createRandomVector3D();
//			
//			forceNodes.put(queryPart, new ForceNode(nodePosition, queryPart));
//		}
//		
//		return forceNodes;
//	}
//
//	private static Vector3D createRandomVector3D() {
//		return new Vector3D(RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0, RAND.nextDouble() * 1000.0);
//	}

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

	private static Map<ILogicalQueryPart, PeerID> determineBestPeers(Map<ILogicalQueryPart, Collection<Bid>> bidQueryPartMap) {
		
		Map<ILogicalQueryPart, PeerID> bestBids = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : bidQueryPartMap.keySet()) {
			bestBids.put(queryPart, determineBestBid(bidQueryPartMap.get(queryPart)).getBidderPeerID());
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
