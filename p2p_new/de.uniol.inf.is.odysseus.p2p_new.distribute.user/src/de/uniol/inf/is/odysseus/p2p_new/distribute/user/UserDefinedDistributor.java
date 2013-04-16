package de.uniol.inf.is.odysseus.p2p_new.distribute.user;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.IPeerManager;
import de.uniol.inf.is.odysseus.p2p_new.P2PNewPlugIn;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

public class UserDefinedDistributor implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(UserDefinedDistributor.class);

	private static final String LOCAL_DESTINATION_NAME = "local";
	private static final String DISTRIBUTION_TYPE = "user";

	private static final String WRAPPER_NAME = "GenericPush";
	private static final String PROTOCOL_HANDLER_NAME = "JxtaSizeByteBuffer";
	private static final String DATA_HANDLER_NAME = "NTuple";
	private static final String ACCESS_NAME = "JxtaAccess_";
	private static final String SENDER_NAME = "JxtaSender_";
	private static final String TRANSPORT_HANDLER_NAME = "Jxta";
	private static final String PIPEID_TAG = "pipeid";

	private static IPQLGenerator generator;
	private static IPeerManager peerManager;

	private static int connectionNumber = 0;

	// called by OSGi-DS
	public final void bindPeerManager(IPeerManager pm) {
		peerManager = pm;

		LOG.debug("PeerManager bound {}", pm);
	}

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

		final Collection<String> remotePeerIDs = peerManager.getRemotePeerIDs();
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
			final Map<QueryPart, String> queryPartDistributionMap = assignQueryParts(remotePeerIDs, peerManager.getOwnPeerName(), queryParts);
			insertSenderAndAccess(queryPartDistributionMap);

			final List<QueryPart> localQueryParts = shareParts(queryPartDistributionMap, sharedQueryID, transCfgName);
			final Collection<ILogicalQuery> logicalQueries = transformToQueries(localQueryParts, generator);
			QueryPartController.getInstance().registerAsMaster(logicalQueries, sharedQueryID);

			localQueries.addAll(logicalQueries);
		}

		return localQueries;
	}

	@Override
	public String getName() {
		return DISTRIBUTION_TYPE;
	}

	// called by OSGi-DS
	public final void unbindPeerManager(IPeerManager pm) {
		if (peerManager == pm) {
			peerManager = null;

			LOG.debug("PeerManager unbound {}", pm);
		}
	}

	// called by OSGi-DS
	public final void unbindPQLGenerator(IPQLGenerator gen) {
		if (generator == gen) {
			generator = null;

			LOG.debug("PQLGenerator unbound {}", gen);
		}
	}

//	private static SDFSchema appendOutputSchemaWithTimestamps(SDFSchema base, String sourceName) {
//		final List<SDFAttribute> attributes = Lists.newArrayList(base.getAttributes());
//		attributes.add(new SDFAttribute(sourceName, "start", SDFDatatype.START_TIMESTAMP));
//		attributes.add(new SDFAttribute(sourceName, "end", SDFDatatype.END_TIMESTAMP));
//		return new SDFSchema("", attributes);
//	}

	private static Map<QueryPart, String> assignQueryParts(Collection<String> remotePeerIDs, String localPeerID, List<QueryPart> queryParts) {
		final Map<QueryPart, String> distributed = Maps.newHashMap();
		final Map<String, String> assignedDestinations = Maps.newHashMap();

		assignedDestinations.put(LOCAL_DESTINATION_NAME, localPeerID);
		for (final String remotePeerID : remotePeerIDs) {
			final Optional<String> optRemotePeerName = peerManager.getPeerName(remotePeerID);
			if (optRemotePeerName.isPresent()) {
				assignedDestinations.put(optRemotePeerName.get(), remotePeerID);
			}
		}

		final List<String> remotePeerIDList = Lists.newArrayList(remotePeerIDs);
		int peerCounter = 0;
		for (final QueryPart queryPart : queryParts) {
			final String destinationName = queryPart.getDestinationName();

			String assignedPeer = assignedDestinations.get(destinationName);
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

	private static Map<String, String> createOptionsMap(PipeID pipeID) {
		final Map<String, String> options = Maps.newHashMap();
		options.put(PIPEID_TAG, pipeID.toString());
		return options;
	}

	private static Map<ILogicalOperator, String> determineDestinationNames(List<ILogicalOperator> operators) {
		final Map<ILogicalOperator, String> destinationNames = Maps.newHashMap();

		for (final ILogicalOperator operator : operators) {
			destinationNames.put(operator, getDestinationName(operator));
		}

		return destinationNames;
	}

	private static Map<QueryPart, ILogicalOperator> determineNextQueryParts(QueryPart currentQueryPart, ILogicalOperator relativeSink, Map<QueryPart, String> queryPartDistributionMap) {
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

			final QueryPart part = new QueryPart(partOperators, chosenDestination, baseName + "_" + parts.size());
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

		final AccessAO access = new AccessAO();
		access.setSource(pipeID.toString());
		access.setWrapper(WRAPPER_NAME);
		access.setTransportHandler(TRANSPORT_HANDLER_NAME);
		access.setProtocolHandler(PROTOCOL_HANDLER_NAME);
		access.setDataHandler(DATA_HANDLER_NAME);
		access.setOptions(createOptionsMap(pipeID));
		access.setOutputSchema(startOperator.getOutputSchema());
		access.setName(ACCESS_NAME + connectionNumber);
//		final ProjectAO projectAO = new ProjectAO();
//		projectAO.setOutputSchemaWithList(truncOutputSchemaFromTimestamps(access.getOutputSchema()));
//		projectAO.addParameterInfo("ATTRIBUTES", toString(projectAO.getOutputSchema()));

		final SenderAO sender = new SenderAO();
		sender.setSink(pipeID.toString());
		sender.setWrapper(WRAPPER_NAME);
		sender.setTransportHandler(TRANSPORT_HANDLER_NAME);
		sender.setProtocolHandler(PROTOCOL_HANDLER_NAME);
		sender.setDataHandler(DATA_HANDLER_NAME);
		sender.setOptions(createOptionsMap(pipeID));
		sender.setName(SENDER_NAME + connectionNumber);
//		final TimestampToPayloadAO payloadAO = new TimestampToPayloadAO();

		final LogicalSubscription removingSubscription = determineSubscription(startOperator, endOperator);
		startOperator.unsubscribeSink(removingSubscription);

		startOperator.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), startOperator.getOutputSchema());
//		payloadAO.subscribeSink(sender, 0, 0, payloadAO.getOutputSchema());

//		projectAO.subscribeToSource(access, 0, 0, access.getOutputSchema());
		endOperator.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		startPart.addSenderAO(sender, startOperator);
		endPart.addAccessAO(access, endOperator);
	}

	private static ID generateSharedQueryID() {
		final String timestamp = String.valueOf(System.currentTimeMillis());
		return IDFactory.newContentID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID(), false, timestamp.getBytes());
	}

	private static String getDestinationName(ILogicalOperator operator) {
		if (!Strings.isNullOrEmpty(operator.getDestinationName())) {
			return operator.getDestinationName();
		}
		if (operator.getSubscribedToSource().size() > 0) {
			return getDestinationName(operator.getSubscribedToSource().iterator().next().getTarget());
		}
		
		return LOCAL_DESTINATION_NAME;
	}

	private static void insertSenderAndAccess(Map<QueryPart, String> queryPartDistributionMap) {
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

	private static void logPeerStatus(Collection<String> peerIDs) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());
			for (final String peerID : peerIDs) {
				LOG.debug("\tPeer: {}", peerID);
			}
		}
	}

	private static void publish(QueryPart part, String destinationPeer, ID sharedQueryID, String transCfgName) {
		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
		part.removeDestinationName();

		final QueryPartAdvertisement adv = (QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));
		adv.setPeerID(toPeerID(destinationPeer));
		adv.setPqlStatement(generator.generatePQLStatement(part.getOperators().iterator().next()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfgName);

		tryPublishImpl(adv);
		LOG.debug("QueryPart {} published", part);
	}

	private static List<QueryPart> shareParts(Map<QueryPart, String> queryPartDistributionMap, ID sharedQueryID, String transCfgName) {
		final List<QueryPart> localParts = Lists.newArrayList();

		final String ownPeerID = peerManager.getOwnPeerName();

		for (final QueryPart part : queryPartDistributionMap.keySet()) {
			final String assignedPeerID = queryPartDistributionMap.get(part);
			if (assignedPeerID.equals(ownPeerID)) {
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
			} else {
				publish(part, assignedPeerID, sharedQueryID, transCfgName);
			}
		}

		return localParts;
	}

	private static PeerID toPeerID(String text) {
		try {
			final URI id = new URI(text);
			return PeerID.create(id);
		} catch (URISyntaxException | ClassCastException ex) {
			LOG.error("Could not transform to pipeid: {}", text, ex);
			return null;
		}
	}

//	private static String toString(SDFSchema outputSchema) {
//		final StringBuilder sb = new StringBuilder();
//		sb.append("[");
//		final List<SDFAttribute> attributes = outputSchema.getAttributes();
//		for (int i = 0; i < attributes.size(); i++) {
//			sb.append("'").append(attributes.get(i).getAttributeName()).append("'");
//			if (i < attributes.size() - 1) {
//				sb.append(", ");
//			}
//		}
//		sb.append("]");
//		return sb.toString();
//	}

	private static Collection<ILogicalQuery> transformToQueries(List<QueryPart> localQueryParts, IPQLGenerator generator) {
		final List<ILogicalQuery> localQueries = Lists.newArrayList();

		for (final QueryPart queryPart : localQueryParts) {
			localQueries.add(queryPart.toLogicalQuery(generator));
		}

		return localQueries;
	}

//	private static List<SDFAttribute> truncOutputSchemaFromTimestamps(SDFSchema outputSchema) {
//		// annahme, die beiden neuen attribute sind ganz hinten..
//		final List<SDFAttribute> newSchema = Lists.newArrayList(outputSchema.getAttributes());
//		newSchema.remove(newSchema.size() - 1);
//		newSchema.remove(newSchema.size() - 1);
//		return newSchema;
//	}

	private static void tryPublishImpl(QueryPartAdvertisement adv) {
		try {
			P2PNewPlugIn.getDiscoveryService().publish(adv, 10000, 10000);
		} catch (final IOException ex) {
			LOG.error("Could not publish query part", ex);
		}
	}
}
