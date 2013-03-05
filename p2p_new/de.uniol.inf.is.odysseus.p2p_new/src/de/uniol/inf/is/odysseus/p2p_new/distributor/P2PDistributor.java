package de.uniol.inf.is.odysseus.p2p_new.distributor;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.Advertisement;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import net.jxta.protocol.PeerAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.handler.JxtaTransportHandler;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class P2PDistributor implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(P2PDistributor.class);
	private static final Random RAND = new Random();
	private static final String LOCAL_DESTINATION_NAME = "local";

	private static final String WRAPPER_NAME = "GenericPush";
	private static final String PROTOCOL_HANDLER_NAME = "SizeByteBuffer";
	private static final String DATA_HANDLER_NAME = "Tuple";
	private static final String ACCESS_NAME = "JxtaAccess_";
	private static final String SENDER_NAME = "JxtaSender_";

	private static IPQLGenerator generator;

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute) {

		if (queriesToDistribute == null || queriesToDistribute.isEmpty()) {
			return queriesToDistribute;
		}

		List<PeerID> remotePeers = determineRemotePeers();
		if (remotePeers.isEmpty()) {
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queriesToDistribute;
		}

		debugPeerStatus(remotePeers);

		List<ILogicalQuery> localQueries = Lists.newArrayList();

		for (ILogicalQuery query : queriesToDistribute) {

			List<ILogicalOperator> operators = Lists.newArrayList();
			collectOperators(query.getLogicalPlan(), operators);
			filterOperators(operators);

			List<QueryPart> queryParts = determineQueryParts(operators, query.toString());
			LOG.debug("Got {} parts of logical query {}", queryParts.size(), query);
			if (queryParts.size() == 1 && queryParts.get(0).getDestinationName().equals(LOCAL_DESTINATION_NAME)) {
				localQueries.add(query);
				LOG.debug("This one part is executed locally");
				continue;
			}

			ID sharedQueryID = generateSharedQueryID();
			Map<QueryPart, PeerID> queryPartDistributionMap = assignQueryParts(remotePeers, P2PNewPlugIn.getOwnPeerID(), queryParts);

			insertSenderAndAccess(queryPartDistributionMap);

			List<QueryPart> localQueryParts = shareParts(queryPartDistributionMap, sharedQueryID);
			localQueries.addAll(transformToQueries(localQueryParts, generator));
		}

		return localQueries;
	}

	private static ID generateSharedQueryID() {
		return IDFactory.newContentID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), true);
	}

	public final void bindPQLGenerator(IPQLGenerator gen) {
		generator = gen;

		LOG.debug("PQLGenerator bound {}", gen);
	}

	public final void unbindPQLGenerator(IPQLGenerator gen) {
		if (generator == gen) {
			generator = null;

			LOG.debug("PQLGenerator unbound {}", gen);
		}
	}

	private static Collection<? extends ILogicalQuery> transformToQueries(List<QueryPart> localQueryParts, IPQLGenerator generator) {
		List<ILogicalQuery> localQueries = Lists.newArrayList();

		for (QueryPart queryPart : localQueryParts) {
			localQueries.add(queryPart.toLogicalQuery(generator));
		}

		return localQueries;
	}

	private static List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID) {
		List<QueryPart> localParts = Lists.newArrayList();

		PeerID ownPeerID = P2PNewPlugIn.getOwnPeerID();

		for (QueryPart part : queryPartDistributionMap.keySet()) {
			PeerID assignedPeerID = queryPartDistributionMap.get(part);
			if (assignedPeerID.equals(ownPeerID)) {
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
			} else {
				publish(part, assignedPeerID, sharedQueryID);
			}
		}

		return localParts;
	}

	public static void publish(QueryPart part, PeerID destinationPeer, ID sharedQueryID) {
		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
		part.removeDestinationName();

		QueryPartAdvertisement adv = (QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));
		adv.setPeerID(destinationPeer);
		adv.setPqlStatement(generator.generatePQLStatement(part.getOperators().iterator().next()));
		adv.setSharedQueryID(sharedQueryID);

		tryPublishImpl(adv);
		LOG.debug("QueryPart {} published", part);
	}

	private static void filterOperators(List<ILogicalOperator> operators) {
		List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		for (ILogicalOperator operator : operators) {
			if (operator instanceof TopAO) {
				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);
			}
		}

		for (ILogicalOperator operatorToRemove : operatorsToRemove) {
			operators.remove(operatorToRemove);
		}
	}

	private static Map<QueryPart, PeerID> assignQueryParts(List<PeerID> remotePeers, PeerID localPeer, List<QueryPart> queryParts) {
		Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		Map<String, PeerID> assignedDestinations = Maps.newHashMap();
		assignedDestinations.put(LOCAL_DESTINATION_NAME, localPeer);

		int peerCounter = 0;
		for (QueryPart queryPart : queryParts) {
			String destinationName = queryPart.getDestinationName();

			PeerID assignedPeer = assignedDestinations.get(destinationName);
			if (assignedPeer == null) {
				// destination has currently no peer assigned
				assignedPeer = remotePeers.get(peerCounter);
				// using round robin to assign peers
				peerCounter = (peerCounter + 1) % remotePeers.size();
				assignedDestinations.put(destinationName, assignedPeer);
			}

			queryPart.removeDestinationName();
			distributed.put(queryPart, assignedPeer);

			LOG.debug("Assign query part {} to peer {}", queryPart, assignedPeer);
		}

		return distributed;
	}

	private static void insertSenderAndAccess(Map<QueryPart, PeerID> queryPartDistributionMap) {
		for (QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for (ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				Map<QueryPart, ILogicalOperator> nextOperators = determineNextQueryParts(relativeSink, queryPartDistributionMap);
				if (!nextOperators.isEmpty()) {
					for (QueryPart destQueryPart : nextOperators.keySet()) {
						generatePeerConnection(relativeSink, queryPart, nextOperators.get(destQueryPart), destQueryPart);
					}
				}
			}

		}
	}

	private static Map<QueryPart, ILogicalOperator> determineNextQueryParts(ILogicalOperator relativeSink, Map<QueryPart, PeerID> queryPartDistributionMap) {
		Map<QueryPart, ILogicalOperator> next = Maps.newHashMap();
		if (relativeSink.getSubscriptions().size() > 0) {

			for (LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				ILogicalOperator target = subscription.getTarget();
				QueryPart targetQueryPart = findLogicalOperator(target, queryPartDistributionMap.keySet());
				next.put(targetQueryPart, target);
			}
		}
		return next;
	}

	private static QueryPart findLogicalOperator(ILogicalOperator target, Set<QueryPart> parts) {
		for (QueryPart part : parts) {
			if (part.containsRelativeSource(target)) {
				return part;
			}
		}
		// should not happen here
		return null;
	}

	private static int connectionNumber = 0;

	private static void generatePeerConnection(ILogicalOperator startOperator, QueryPart startPart, ILogicalOperator endOperator, QueryPart endPart) {
		PipeID pipeID = (PipeID) IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID());

		AccessAO access = new AccessAO();
		access.setSource(pipeID.toString());
		access.setWrapper(WRAPPER_NAME);
		access.setTransportHandler(JxtaTransportHandler.NAME);
		access.setProtocolHandler(PROTOCOL_HANDLER_NAME);
		access.setDataHandler(DATA_HANDLER_NAME);
		access.setOptions(createOptionsMap(pipeID));
		access.setOutputSchema(startOperator.getOutputSchema());
		access.setName(ACCESS_NAME + connectionNumber);

		SenderAO sender = new SenderAO();
		sender.setSink(pipeID.toString());
		sender.setWrapper(WRAPPER_NAME);
		sender.setTransportHandler(JxtaTransportHandler.NAME);
		sender.setProtocolHandler(PROTOCOL_HANDLER_NAME);
		sender.setDataHandler(DATA_HANDLER_NAME);
		sender.setOptions(createOptionsMap(pipeID));
		sender.setName(SENDER_NAME + connectionNumber);

		LogicalSubscription removingSubscription = determineSubscription(startOperator, endOperator);

		startOperator.unsubscribeSink(removingSubscription);
		startOperator.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), startOperator.getOutputSchema());
		endOperator.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		startPart.addSenderAO(sender, startOperator);
		endPart.addAccessAO(access, endOperator);
	}

	private static LogicalSubscription determineSubscription(ILogicalOperator startOperator, ILogicalOperator endOperator) {
		for (LogicalSubscription subscription : startOperator.getSubscriptions()) {
			if (subscription.getTarget().equals(endOperator)) {
				return subscription;
			}
		}
		return null;
	}

	private static Map<String, String> createOptionsMap(PipeID pipeID) {
		Map<String, String> options = Maps.newHashMap();
		options.put(JxtaTransportHandler.PIPEID_TAG, pipeID.toString());
		return options;
	}

	private static List<QueryPart> determineQueryParts(List<ILogicalOperator> operators, String baseName) {
		List<QueryPart> parts = Lists.newArrayList();

		Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(operators);

		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator chosenOperator = chooseOneOperator(operatorsToVisit);
			String chosenDestination = destinations.get(chosenOperator);

			List<ILogicalOperator> partOperators = Lists.newArrayList();
			collectOperatorsWithEqualDestination(chosenOperator, chosenDestination, partOperators, destinations);

			operatorsToVisit.removeAll(partOperators);

			QueryPart part = new QueryPart(partOperators, chosenDestination, baseName + "_" + parts.size());
			parts.add(part);
		}

		return parts;
	}

	private static List<PeerID> determineRemotePeers() {
		try {
			List<PeerID> foundPeers = Lists.newArrayList();

			Enumeration<Advertisement> peerAdvertisements = P2PNewPlugIn.getDiscoveryService().getLocalAdvertisements(DiscoveryService.PEER, null, null);
			while (peerAdvertisements.hasMoreElements()) {
				PeerAdvertisement adv = (PeerAdvertisement) peerAdvertisements.nextElement();
				if (!adv.getPeerID().equals(P2PNewPlugIn.getOwnPeerID())) {
					foundPeers.add(adv.getPeerID());
				}
			}

			return foundPeers;
		} catch (IOException ex) {
			LOG.error("Could not get peers", ex);
			return Lists.newArrayList();
		}
	}

	private static void collectOperatorsWithEqualDestination(ILogicalOperator operator, String chosenDestination, List<ILogicalOperator> collectedOperators, Map<ILogicalOperator, String> destinations) {
		if (collectedOperators.contains(operator)) {
			return;
		}

		if (destinations.get(operator).equalsIgnoreCase(chosenDestination)) {

			collectedOperators.add(operator);

			for (LogicalSubscription subscription : operator.getSubscriptions()) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}

			for (LogicalSubscription subscription : operator.getSubscribedToSource()) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}
		}
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(List<ILogicalOperator> operators) {
		Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (ILogicalOperator operator : operators) {
			destinationNames.put(operator, getDestinationName(operator));
		}

		return destinationNames;
	}

	private static String getDestinationName(ILogicalOperator operator) {
		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		} else {
			if (operator.getSubscribedToSource().size() > 0) {
				return getDestinationName(operator.getSubscribedToSource().iterator().next().getTarget());
			} else {
				return LOCAL_DESTINATION_NAME;
			}
		}
	}

	private static ILogicalOperator chooseOneOperator(List<ILogicalOperator> operatorsToVisit) {
		int i = RAND.nextInt(operatorsToVisit.size());
		return operatorsToVisit.get(i);
	}

	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {

			list.add(currentOperator);

			for (LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperators(subscription.getTarget(), list);
			}

			for (LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperators(subscription.getTarget(), list);
			}
		}
	}

	private static void debugPeerStatus(List<PeerID> peers) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found {} peers to distribute the queries", peers.size());
			for (PeerID peer : peers) {
				LOG.debug("Peer: {}", peer);
			}
		}
	}

	private static void tryPublishImpl(QueryPartAdvertisement adv) {
		try {
			P2PNewPlugIn.getDiscoveryService().publish(adv, 5000, 5000);
		} catch (IOException ex) {
			LOG.error("Could not publish query part", ex);
		}
	}
}
