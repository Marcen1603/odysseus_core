package de.uniol.inf.is.odysseus.p2p_new.distribute.user;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.p2p_new.distribute.user.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.user.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class UserDefinedDistributor implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(UserDefinedDistributor.class);

	private static final String LOCAL_DESTINATION_NAME = "local";
	private static final String DISTRIBUTION_TYPE = "user";
	private static final String PARSER_ID = "PQL";

	private static final String ACCESS_NAME = "JxtaReceiver_";
	private static final String SENDER_NAME = "JxtaSender_";
	
	private static IPQLGenerator generator;

	private static int connectionNumber = 0;

	// called by OSGi-DS
	public final void bindPQLGenerator(IPQLGenerator gen) {
		generator = gen;

		LOG.debug("PQLGenerator bound {}", gen);
	}

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender, List<ILogicalQuery> queriesToDistribute, String transCfgName) {

		if (queriesToDistribute == null || queriesToDistribute.isEmpty()) {
			return queriesToDistribute;
		}

		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		if (remotePeerIDs.isEmpty()) {
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queriesToDistribute;
		}

		logPeerStatus(remotePeerIDs);

		final List<ILogicalQuery> localQueries = Lists.newArrayList();

		for (final ILogicalQuery query : queriesToDistribute) {

			final List<ILogicalOperator> operators = Lists.newArrayList();
			collectOperators(query.getLogicalPlan(), operators);
			filterOperators(operators);

			final List<QueryPart> queryParts = determineQueryParts(operators, query.toString());
			LOG.debug("Got {} parts of logical query {}", queryParts.size(), query);
			if (queryParts.size() == 1 && queryParts.get(0).getDestinationName().equals(LOCAL_DESTINATION_NAME)) {
				localQueries.add(query);
				LOG.debug("This one part is executed locally");
				continue;
			}

			final ID sharedQueryID = generateSharedQueryID();
			final Map<QueryPart, PeerID> queryPartDistributionMap = assignQueryParts(remotePeerIDs, P2PDictionaryService.get().getLocalPeerID(), queryParts);
			insertSenderAndAccess(queryPartDistributionMap);

			final List<QueryPart> localQueryParts = shareParts(queryPartDistributionMap, sharedQueryID, transCfgName);
			final ILogicalQuery logicalQuery = transformToQuery(localQueryParts, generator, query.toString());
						
			QueryPartController.getInstance().registerAsMaster(logicalQuery, sharedQueryID);

			localQueries.add(logicalQuery);
		}

		return localQueries;
	}

	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}

	// called by OSGi-DS
	public final void unbindPQLGenerator(IPQLGenerator gen) {
		if (generator == gen) {
			generator = null;

			LOG.debug("PQLGenerator unbound {}", gen);
		}
	}

	private static Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, PeerID localPeerID, List<QueryPart> queryParts) {
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		final Map<String, PeerID> assignedDestinations = Maps.newHashMap();

		assignedDestinations.put(LOCAL_DESTINATION_NAME, localPeerID);
		for (final PeerID remotePeerID : remotePeerIDs) {
			final Optional<String> optRemotePeerName = P2PDictionaryService.get().getPeerRemoteName(remotePeerID);
			if (optRemotePeerName.isPresent()) {
				assignedDestinations.put(optRemotePeerName.get(), remotePeerID);
			}
		}

		final List<PeerID> remotePeerIDList = Lists.newArrayList(remotePeerIDs);
		int peerCounter = 0;
		for (final QueryPart queryPart : queryParts) {
			final String destinationName = queryPart.getDestinationName().get();

			PeerID assignedPeer = assignedDestinations.get(destinationName);
			if (assignedPeer == null) {
				// destination has currently no peer assigned
				assignedPeer = remotePeerIDList.get(peerCounter);
				// using round robin to assign peers
				peerCounter = (peerCounter + 1) % remotePeerIDList.size();
				assignedDestinations.put(destinationName, assignedPeer);
			}

			queryPart.removeDestinationName();
			distributed.put(queryPart, assignedPeer);

			LOG.debug("Assign query part {} to peer {}", queryPart, assignedPeer);
		}

		return distributed;
	}

	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {

			list.add(currentOperator);

			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperators(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperators(subscription.getTarget(), list);
			}
		}
	}

	private static void collectOperatorsWithEqualDestination(ILogicalOperator operator, String chosenDestination, List<ILogicalOperator> collectedOperators, Map<ILogicalOperator, String> destinations) {
		if (collectedOperators.contains(operator)) {
			return;
		}

		if (destinations.get(operator).equalsIgnoreCase(chosenDestination)) {

			collectedOperators.add(operator);

			for (final LogicalSubscription subscription : operator.getSubscriptions()) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}

			for (final LogicalSubscription subscription : operator.getSubscribedToSource()) {
				collectOperatorsWithEqualDestination(subscription.getTarget(), chosenDestination, collectedOperators, destinations);
			}
		}
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(List<ILogicalOperator> operators) {
		final Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (final ILogicalOperator operator : operators) {
			destinationNames.put(operator, getDestinationName(operator));
		}

		return destinationNames;
	}

	private static Map<QueryPart, ILogicalOperator> determineNextQueryParts(QueryPart currentQueryPart, ILogicalOperator relativeSink, Map<QueryPart, PeerID> queryPartDistributionMap) {
		final Map<QueryPart, ILogicalOperator> next = Maps.newHashMap();
		if (relativeSink.getSubscriptions().size() > 0) {

			for (final LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				final ILogicalOperator target = subscription.getTarget();
				if (!currentQueryPart.getOperators().contains(target)) {
					final QueryPart targetQueryPart = findLogicalOperator(target, queryPartDistributionMap.keySet());
					next.put(targetQueryPart, target);
				}
			}
		}
		return next;
	}

	private static List<QueryPart> determineQueryParts(List<ILogicalOperator> operators, String baseName) {
		final List<QueryPart> parts = Lists.newArrayList();

		final Map<ILogicalOperator, String> destinations = determineDestinationNames(operators);
		final List<ILogicalOperator> operatorsToVisit = Lists.newArrayList(operators);

		while (!operatorsToVisit.isEmpty()) {
			final ILogicalOperator chosenOperator = operatorsToVisit.get(0);
			final String chosenDestination = destinations.get(chosenOperator);

			final List<ILogicalOperator> partOperators = Lists.newArrayList();
			collectOperatorsWithEqualDestination(chosenOperator, chosenDestination, partOperators, destinations);

			operatorsToVisit.removeAll(partOperators);

			final QueryPart part = new QueryPart(partOperators, chosenDestination);
			parts.add(part);
		}

		return parts;
	}

	private static LogicalSubscription determineSubscription(ILogicalOperator startOperator, ILogicalOperator endOperator) {
		for (final LogicalSubscription subscription : startOperator.getSubscriptions()) {
			if (subscription.getTarget().equals(endOperator)) {
				return subscription;
			}
		}
		return null;
	}

	private static void filterOperators(List<ILogicalOperator> operators) {
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		for (final ILogicalOperator operator : operators) {
			if (operator instanceof TopAO) {
				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);
			}
		}

		for (final ILogicalOperator operatorToRemove : operatorsToRemove) {
			operators.remove(operatorToRemove);
		}
	}

	private static QueryPart findLogicalOperator(ILogicalOperator target, Set<QueryPart> parts) {
		for (final QueryPart part : parts) {
			if (part.containsRelativeSource(target)) {
				return part;
			}
		}

		throw new IllegalArgumentException("Could not find query part for logical operator " + target);
	}

	private static void generatePeerConnection(ILogicalOperator startOperator, QueryPart startPart, ILogicalOperator endOperator, QueryPart endPart) {
		final PipeID pipeID = IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID());

		final JxtaReceiverAO access = new JxtaReceiverAO();
		access.setPipeID(pipeID.toString());
		access.setOutputSchema(generateOutputSchema(ACCESS_NAME + connectionNumber, startOperator.getOutputSchema()));
		access.setSchema(startOperator.getOutputSchema().getAttributes());
		access.setName(ACCESS_NAME + connectionNumber );

		final JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPipeID(pipeID.toString());
		sender.setName(SENDER_NAME + connectionNumber );

		final LogicalSubscription removingSubscription = determineSubscription(startOperator, endOperator);
		startOperator.unsubscribeSink(removingSubscription);

		startOperator.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), startOperator.getOutputSchema());
		endOperator.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		startPart.addSenderAO(sender, startOperator);
		endPart.addAccessAO(access, endOperator);
		
		connectionNumber++;
	}

	private static SDFSchema generateOutputSchema(String basename, SDFSchema outputSchema) {
		List<SDFAttribute> attributes = Lists.newArrayList();
		for( SDFAttribute attribute : outputSchema ) {
			attributes.add(new SDFAttribute(basename, attribute.getAttributeName(), attribute));
		}
		return new SDFSchema(basename, attributes);
	}

	private static ID generateSharedQueryID() {
		final String timestamp = String.valueOf(System.currentTimeMillis());
		return IDFactory.newContentID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), false, timestamp.getBytes());
	}

	private static String getDestinationName(ILogicalOperator operator) {
		if( operator instanceof StreamAO ) {
			return LOCAL_DESTINATION_NAME;
		}
		
		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		}
		if (operator.getSubscribedToSource().size() > 0) {
			return getDestinationName(operator.getSubscribedToSource().iterator().next().getTarget());
		}
		
		return LOCAL_DESTINATION_NAME;
	}

	private static void insertSenderAndAccess(Map<QueryPart, PeerID> queryPartDistributionMap) {
		for (final QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for (final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				final Map<QueryPart, ILogicalOperator> nextOperators = determineNextQueryParts(queryPart, relativeSink, queryPartDistributionMap);
				if (!nextOperators.isEmpty()) {
					for (final QueryPart destQueryPart : nextOperators.keySet()) {
						generatePeerConnection(relativeSink, queryPart, nextOperators.get(destQueryPart), destQueryPart);
					}
				}
			}

		}
	}

	private static void logPeerStatus(Collection<PeerID> peerIDs) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());
			for (final PeerID peerID : peerIDs) {
				LOG.debug("\tPeer: {}", peerID);
			}
		}
	}

	private static void publish(QueryPart part, PeerID destinationPeer, ID sharedQueryID, String transCfgName) {
		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
		part.removeDestinationName();

		final QueryPartAdvertisement adv = (QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));
		adv.setPeerID(destinationPeer);
		adv.setPqlStatement(generator.generatePQLStatement(part.getOperators().iterator().next()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfgName);

		tryPublishImpl(adv);
		LOG.debug("QueryPart {} published", part);
	}

	private static List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, String transCfgName) {
		final List<QueryPart> localParts = Lists.newArrayList();

		final PeerID ownPeerID = P2PDictionaryService.get().getLocalPeerID();

		for (final QueryPart part : queryPartDistributionMap.keySet()) {
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);
			if (assignedPeerID.equals(ownPeerID)) {
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
			} else {
				publish(part, assignedPeerID, sharedQueryID, transCfgName);
			}
		}

		return localParts;
	}

	private static ILogicalQuery transformToQuery(List<QueryPart> queryParts, IPQLGenerator generator, String name) {
		final Collection<ILogicalOperator> sinks = collectSinks(queryParts);
		final TopAO topAO = generateTopAO(sinks);
		
		final ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(topAO, true);
		logicalQuery.setName(name);
		logicalQuery.setParserId(PARSER_ID);
		logicalQuery.setPriority(0);
		logicalQuery.setUser(SessionManagementService.getActiveSession());
		logicalQuery.setQueryText(generator.generatePQLStatement(topAO));
		
		return logicalQuery;
	}

	private static Collection<ILogicalOperator> collectSinks(List<QueryPart> queryParts) {
		final Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for( QueryPart queryPart : queryParts ) {
			sinks.addAll(queryPart.getRealSinks());
		}
		return sinks;
	}

	private static TopAO generateTopAO(final Collection<ILogicalOperator> sinks) {
		final TopAO topAO = new TopAO();
		int inputPort = 0;
		for( ILogicalOperator sink : sinks ) {
			topAO.subscribeToSource(sink, inputPort++, 0, sink.getOutputSchema());
		}
		return topAO;
	}

	private static void tryPublishImpl(QueryPartAdvertisement adv) {
		try {
			JxtaServicesProviderService.get().getDiscoveryService().publish(adv, 10000, 10000);
		} catch (final IOException ex) {
			LOG.error("Could not publish query part", ex);
		}
	}
}
