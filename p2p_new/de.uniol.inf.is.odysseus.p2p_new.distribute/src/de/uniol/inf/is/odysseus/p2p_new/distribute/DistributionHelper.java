package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.p2p_new.IPeerAssignment;
import de.uniol.inf.is.odysseus.p2p_new.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.peerAssignment.RRPeerAssignment;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartController;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PNetworkManagerService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.PeerAssignmentProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.p2p_new.parameter.PeerAssignmentParameter;

/**
 * Utilities for the usage of the interface {@link ILogicalQueryDistributor}.
 * @author Michael Brand
 */
public class DistributionHelper {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DistributionHelper.class);

	/**
	 * The string representation of the PQL parser ID.
	 */
	public static final String PQL_PARSER_ID = "PQL";

	/**
	 * The beginning of all names for {@link JxtaReceiverAO}s. <br />
	 * It will be enhanced by identifying numbers.
	 */
	public static final String ACCESS_BASE_NAME = "JxtaReceiver_";

	/**
	 * The beginning of all names for {@link JxtaSenderAO}s. <br />
	 * It will be enhanced by identifying numbers.
	 */
	public static final String SENDER_BASE_NAME = "JxtaSender_";

	/**
	 * The name for query parts to be executed locally.
	 * @see QueryPart#getDestinationName()
	 */
	public static final String LOCAL_DESTINATION_NAME = "local";

	/**
	 * The number of the next connection to be generated. <br />
	 * This number will enhance {@link #ACCESS_BASE_NAME} and {@link #SENDER_BASE_NAME}.
	 */
	private static int globalConnectionCounter = 0;

	/**
	 * Returns the name of a peer given by ID, if present.
	 */
	public static Optional<String> getPeerName(PeerID peerID) {

		Preconditions.checkNotNull(peerID);

		return P2PDictionaryService.get().getRemotePeerName(peerID);

	}

	/**
	 * Returns the ID of the local peer.
	 */
	public static PeerID getLocalPeerID() {

		return P2PNetworkManagerService.get().getLocalPeerID();

	}

	/**
	 * Returns a collection of all available remote peers.
	 */
	public static Collection<PeerID> getAvailableRemotePeers() {

		// The return value
		Collection<PeerID> peerIDs = P2PDictionaryService.get().getRemotePeerIDs();

		if(LOG.isDebugEnabled()) {

			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());

			for(final PeerID peerID : peerIDs) {

				// The name of the peer if present
				Optional<String> peerName = P2PDictionaryService.get().getRemotePeerName(peerID);

				if(peerName.isPresent())
					LOG.debug("\tPeer: {}", peerName.get());
				else LOG.debug("\tPeer: {}", peerID);

			}

		}

		return peerIDs;

	}

	/**
	 * Makes a copy of a logical query.
	 */
	public static ILogicalQuery copyLogicalQuery(ILogicalQuery originQuery) {

		Preconditions.checkNotNull(originQuery);

		// The return value
		ILogicalQuery copy = new LogicalQuery(PQL_PARSER_ID,
				DistributionHelper.copyLogicalPlan(originQuery.getLogicalPlan()),
				originQuery.getPriority());

		copy.setName(originQuery.getName());
		copy.setQueryText(PQLGeneratorService.get().generatePQLStatement(copy.getLogicalPlan()));
		copy.setUser(originQuery.getUser());

		return copy;

	}

	/**
	 * Generates all necessary peer connections.
	 * @param peerIDToQueryPartMap A mapping of the assigned peer IDs to the query parts.
	 */
	public static void generatePeerConnections(Map<QueryPart, PeerID> peerIDToQueryPartMap) {

		Preconditions.checkNotNull(peerIDToQueryPartMap);

		for(final QueryPart queryPart : peerIDToQueryPartMap.keySet()) {

			for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {

				// The mapping of all operators next to the sink to their query parts
				final Map<QueryPart, List<ILogicalOperator>> nextOperatorsToQueryPartMap =
						DistributionHelper.determineNextQueryParts(relativeSink, queryPart, peerIDToQueryPartMap.keySet());

				if(!nextOperatorsToQueryPartMap.isEmpty()) {

					for(QueryPart destQueryPart : nextOperatorsToQueryPartMap.keySet()) {

						for( ILogicalOperator targetOperator : nextOperatorsToQueryPartMap.get(destQueryPart)) {
							// Generate connection
							DistributionHelper.generatePeerConnection(queryPart, destQueryPart, relativeSink,
									targetOperator, SENDER_BASE_NAME + globalConnectionCounter,
									ACCESS_BASE_NAME + globalConnectionCounter);
							globalConnectionCounter++;
						}
					}

				}
			}

		}

	}

	/**
	 * Distributes query parts to their assigned peer and transforms all query parts to be executed locally into a logical query.
	 * @param queryParts A collection of query parts to be distributed.
	 * @param queryPartDistributionMap The mapping of assigned peer IDs to query parts.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @param queryName The name of the origin query for transformation.
	 * @return The logical query to be executed locally.
	 */
	public static ILogicalQuery distributeAndTransformParts(Collection<QueryPart> queryParts, Map<QueryPart,
			PeerID> queryPartDistributionMap, QueryBuildConfiguration parameters, String queryName) {

		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(queryParts, "queryParts must be not null!");
		Preconditions.checkNotNull(parameters, "transCfg must be not null!");

		// Generate an ID for the shared query
		final ID sharedQueryID = IDFactory.newContentID(P2PNetworkManagerService.get().getLocalPeerGroupID(), false,
				String.valueOf(System.currentTimeMillis()).getBytes());

		// Get all queryParts of this query mapped with the executing peer
		final Map<QueryPart, PeerID> queryPartPeerMap = Maps.newHashMap();
		for(QueryPart part : queryParts) {

			// The changed query part
			QueryPart preparedPart = null;

			// The changed collection of all operators of the query part
			Collection<ILogicalOperator> operators = Lists.newArrayList();
			operators.addAll(part.getOperators());
			DistributionHelper.replaceStreamAOs(operators);
			if(part.getDestinationName().isPresent())
				preparedPart = new QueryPart(operators, part.getDestinationName().get());
			else preparedPart = new QueryPart(operators);
			queryPartPeerMap.put(preparedPart, queryPartDistributionMap.get(part));

		}

		// publish the queryparts and transform the parts which shall be executed locally into a query
		ILogicalQuery localQuery = DistributionHelper.transformToQuery(distributeParts(queryPartPeerMap, sharedQueryID, parameters), queryName);

		// Registers an sharedQueryID as a master to resolve removed query parts
		QueryPartController.getInstance().registerAsMaster(localQuery, sharedQueryID);

		return localQuery;

	}

	/**
	 * Determine the peer assignment strategy given by the parameters.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 */
	public static IPeerAssignment determinePeerAssignmentStrategy(QueryBuildConfiguration parameters) {

		// The return value
		Optional<IPeerAssignment> peerAssignment = Optional.absent();

		if(parameters.contains(PeerAssignmentParameter.class))
			peerAssignment = PeerAssignmentProviderService.get().getPeerAssignment(
					parameters.get(PeerAssignmentParameter.class).getValue());

		if(peerAssignment.isPresent()) {
			return peerAssignment.get();
		}

		return new RRPeerAssignment();
	}

	/**
	 * Assigns query parts to peers, where they shall be executed, if names of present peers
	 * are set via {@link QueryPart#QueryPart(Collection, String)} . <br />
	 * For all query parts, which have no destination set, nothing will be done.
	 * @param remotePeerIDs A collection of all available peers.
	 * @param queryParts A collection of query parts to be assigned.
	 * @return A mapping of assigned peer IDs to the query parts.
	 */
	public static Map<QueryPart, PeerID> assignPeersDueToGivenDestinations(
			Collection<PeerID> remotePeerIDs, Collection<QueryPart> queryParts) {

		Preconditions.checkNotNull(remotePeerIDs);
		Preconditions.checkArgument(remotePeerIDs.size() > 0);
		Preconditions.checkNotNull(queryParts);

		// The return value
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();

		// The mapping of all available peers to their names
		final Map<String, PeerID> peerIDToNameMap = Maps.newHashMap();
		peerIDToNameMap.put(DistributionHelper.LOCAL_DESTINATION_NAME,
				P2PNetworkManagerService.get().getLocalPeerID());
		for(final PeerID remotePeerID : remotePeerIDs) {

			// The name of the peer
			final Optional<String> peerName =
					P2PDictionaryService.get().getRemotePeerName(remotePeerID);
			if(peerName.isPresent())
				peerIDToNameMap.put(peerName.get(), remotePeerID);

		}

		// The iterator for the query parts.
		final Iterator<QueryPart> partsIter = queryParts.iterator();

		while(partsIter.hasNext()) {

			// The current query part
			QueryPart part = partsIter.next();

			// The name of the assigned peer if present
			Optional<String> peerName;

			// The ID of the assigned peer
			PeerID peerID = null;

			if(part.getDestinationName().isPresent()) {

				peerName = part.getDestinationName();

				if(peerIDToNameMap.containsKey(part.getDestinationName().get())) {

					// peer name found
					peerID = peerIDToNameMap.get(peerName.get());

					distributed.put(part, peerID);

					if(peerName.isPresent())
						LOG.debug("Assign query part {} to peer {}", part, peerName.get());
					else LOG.debug("Assign query part {} to peer {}", part, peerID);

				}

			}

		}

		return distributed;

	}

	/**
	 * Collects all real sinks from a list of query parts.
	 */
	private static Collection<ILogicalOperator> collectSinks(List<QueryPart> queryParts) {

		Preconditions.checkNotNull(queryParts, "queryParts must not be null!");

		// The return value
		final Collection<ILogicalOperator> sinks = Lists.newArrayList();

		for(QueryPart queryPart : queryParts)
			sinks.addAll(queryPart.getRealSinks());

		return sinks;

	}

	/**
	 * Replaces all {@link StreamAO}s by their transformed stream.
	 * @param operators A list of operators. Must be mutable.
	 * @return The list of operators, where each {@link StreamAO} was replaced.
	 */
	private static Collection<ILogicalOperator> replaceStreamAOs(Collection<ILogicalOperator> operators) {

		Preconditions.checkNotNull(operators);

		// The StreamAOs to be removed
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();

		// The transformed streams to be added
		final List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();

		for(ILogicalOperator operator : operators) {

			if(operator instanceof StreamAO) {

				// The transformed stream
				ILogicalOperator streamPlan = ServerExecutorService.getDataDictionary(
						SessionManagementService.getActiveSession().getTenant()).getStreamForTransformation(((StreamAO) operator).getStreamname(),
						SessionManagementService.getActiveSession());

				// A copy of the transformed stream
				ILogicalOperator streamPlanCopy = copyLogicalPlan(streamPlan);

				// Remove StreamAO and insert copy of transformed stream
				RestructHelper.replaceWithSubplan(operator, streamPlanCopy);
				operatorsToRemove.add(operator);
				operatorsToAdd.add(streamPlanCopy);

			}

		}

		operators.removeAll(operatorsToRemove);
		operators.addAll(operatorsToAdd);

		return operators;

	}

	/**
	 * Replaces every {@link StreamAO} within a {@link QueryPart} by its logical subplan.
	 * @param part The {@link QueryPart} where the {@linkStreamAO}s shall be replaced. <br />
	 * <code>part</code> must not be null.
	 * @return The new {@link QueryPlan} without the {@link StreamAO}.
	 */
	public static void replaceStreamAOs(ILogicalOperator plan) {

		Preconditions.checkNotNull(plan,"part must not be null!");

		Collection<ILogicalOperator> operators = DistributionHelper.collectOperators(plan);

		for(ILogicalOperator operator : operators) {

			if(operator instanceof StreamAO) {

				ILogicalOperator streamPlan = ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getStreamForTransformation(((StreamAO) operator).getStreamname(),
						SessionManagementService.getActiveSession());
				ILogicalOperator streamPlanCopy = copyLogicalPlan(streamPlan);
				RestructHelper.replaceWithSubplan(operator, streamPlanCopy);

			}

		}
	}

	/**
	 * Copies a logical plan.
	 * @param originPlan The logical plan to be copied. <br />
	 * <code>originPlan</code> must not be null.
	 * @return A copy of <code>originPlan</code>.
	 * Makes a copy of a logical plan.
	 */
	@SuppressWarnings("unchecked")	// GenericGraphWalker.prefixWalk(ISubscriber, IGraphNodeVisitor)
	private static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {

		Preconditions.checkNotNull(originPlan);

		// The visitor for the logical plan
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());

		// The walker for the logical plan
		@SuppressWarnings("rawtypes")
		GenericGraphWalker walker = new GenericGraphWalker();

		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();

	}

	/**
	 * Determines all query parts, which contain operators subscribed to a relative sink of a given query part.
	 * @param relativeSink The relative sink to process.
	 * @param currentQueryPart The query part, which contains the relative sink.
	 * @param queryParts A list of all query parts.
	 * @return A mapping of all operators subscribed to the relative sink to their query parts.
	 */
	private static Map<QueryPart, List<ILogicalOperator>> determineNextQueryParts(ILogicalOperator relativeSink, QueryPart currentQueryPart,
			Collection<QueryPart> queryParts) {

		Preconditions.checkNotNull(relativeSink);
		Preconditions.checkNotNull(currentQueryPart);
		Preconditions.checkNotNull(queryParts);

		// The return value
		final Map<QueryPart, List<ILogicalOperator>> operatorToQueryPartMap = Maps.newHashMap();

		if(relativeSink.getSubscriptions().size() > 0) {

			for(final LogicalSubscription subscription : relativeSink.getSubscriptions()) {

				// The target operator of the subscription
				final ILogicalOperator target = subscription.getTarget();

				if(!currentQueryPart.getOperators().contains(target)) {

					// Not within the same query part

					// The query part which has to be connected to the processed one
					Optional<QueryPart> nextPart = DistributionHelper.findLogicalOperatorAsRelativeSource(target, queryParts);

					if(nextPart.isPresent()) {
						QueryPart nextQueryPart = nextPart.get();
						if( operatorToQueryPartMap.containsKey(nextQueryPart)) {
							operatorToQueryPartMap.get(nextQueryPart).add(target);
						} else {
							List<ILogicalOperator> targets = Lists.newArrayList();
							targets.add(target);
							operatorToQueryPartMap.put(nextQueryPart, targets);
						}
					}

				}

			}

		}

		return operatorToQueryPartMap;

	}

	/**
	 * Returns that query part, where a given operator is a relative source, or null, if there is none.
	 * @param operator The operator to process.
	 * @param queryParts A list of all query parts.
	 */
	private static Optional<QueryPart> findLogicalOperatorAsRelativeSource(ILogicalOperator operator, Collection<QueryPart> queryParts) {

		Preconditions.checkNotNull(operator);
		Preconditions.checkNotNull(queryParts);

		for(final QueryPart part : queryParts) {

			if(part.containsRelativeSource(operator))
				return Optional.of(part);

		}

		return Optional.absent();

	}

	/**
	 * Generates a peer connection between two query parts.
	 * @param senderPart The query part containing the relative sink.
	 * @param acceptorPart The query part containing the relative source.
	 * @param sinkOfSender The relative sink.
	 * @param sourceOfAcceptor The relative source.
	 * @param senderName The name for the {@link JxtaSenderAO}.
	 * @param accessName The name for the {@link JxtaReceiverAO}.
	 */
	private static void generatePeerConnection(QueryPart senderPart, QueryPart acceptorPart, ILogicalOperator sinkOfSender,
			ILogicalOperator sourceOfAcceptor, String senderName, String accessName) {

		Preconditions.checkNotNull(senderPart);
		Preconditions.checkNotNull(acceptorPart);
		Preconditions.checkNotNull(sinkOfSender);
		Preconditions.checkNotNull(sourceOfAcceptor);
		Preconditions.checkNotNull(senderName);
		Preconditions.checkNotNull(accessName);

		// The ID of the connecting pipe
		final PipeID pipeID = IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID());
		LOG.debug("PipeID {} created", pipeID.toString());

		// The access operator
		final JxtaReceiverAO access = new JxtaReceiverAO();
		access.setPipeID(pipeID.toString());
		access.setOutputSchema(sinkOfSender.getOutputSchema());
		access.setSchema(sinkOfSender.getOutputSchema().getAttributes());
		access.setName(accessName);

		// The sender operator
		final JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPipeID(pipeID.toString());
		sender.setOutputSchema(sinkOfSender.getOutputSchema());
		sender.setName(senderName);

		// The subscription between sink and source to be removed
		final LogicalSubscription removingSubscription = RestructHelper.determineSubscription(sinkOfSender, sourceOfAcceptor);
		sinkOfSender.unsubscribeSink(removingSubscription);

		// Subscribe sender operator to sink and access operator to source
		sinkOfSender.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), sinkOfSender.getOutputSchema());
		sourceOfAcceptor.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		senderPart.addSenderAO(sender, sinkOfSender);
		acceptorPart.addAccessAO(access, sourceOfAcceptor);

	}

	/**
	 * Publishes a query part as an advertisement on a given peer.
	 * @param queryPart The query part to be published.
	 * @param destinationPeerID The ID of the peer, where the query part shall be published.
	 * @param sharedQueryID The ID of the whole distributed query.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 */
	private static void publish(QueryPart queryPart, PeerID destinationPeerID, ID sharedQueryID, QueryBuildConfiguration parameters) {

		Preconditions.checkNotNull(queryPart);
		Preconditions.checkNotNull(destinationPeerID);
		Preconditions.checkNotNull(sharedQueryID);
		Preconditions.checkNotNull(parameters);

		// Create a new advertisement for the query part
		final QueryPartAdvertisement adv =
				(QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PNetworkManagerService.get().getLocalPeerGroupID()));
		adv.setPeerID(destinationPeerID);
		adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(queryPart.getLogicalPlan()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(parameters.getName());

		// Publish the advertisement
		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(destinationPeerID.toString(), adv, 15000);

		// The name of the destination peer if present
		Optional<String> peerName = P2PDictionaryService.get().getRemotePeerName(destinationPeerID);
		if(peerName.isPresent())
			LOG.debug("QueryPart {} remotely published at {}", queryPart, peerName.get());
		else LOG.debug("QueryPart {} remotely published at {}", queryPart, destinationPeerID);

	}

	/**
	 * Transforms a list of query parts to a logical query.
	 * @param queryParts A list of query parts.
	 * @param name The name of the query to be created.
	 */
	private static ILogicalQuery transformToQuery(List<QueryPart> queryParts, String name) {

		Preconditions.checkNotNull(queryParts);
		Preconditions.checkArgument(!queryParts.isEmpty());
		Preconditions.checkNotNull(name);

		// All sinks of all query parts
		final Collection<ILogicalOperator> sinks = DistributionHelper.collectSinks(queryParts);

		// The TopAO to be set above the sinks
		final TopAO topAO = RestructHelper.generateTopAO(sinks);

		// The return value
		final ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(topAO, true);
		logicalQuery.setName(name);
		logicalQuery.setParserId(PQL_PARSER_ID);
		logicalQuery.setPriority(0);
		logicalQuery.setUser(SessionManagementService.getActiveSession());
		logicalQuery.setQueryText(PQLGeneratorService.get().generatePQLStatement(topAO));

		return logicalQuery;

	}

	/**
	 * Distributes query parts to their assigned peer.
	 * @param queryPartDistributionMap The mapping of assigned peer IDs to query parts.
	 * @param sharedQueryID The shared query id.
	 * @param parameters The {@link QueryBuildConfiguration}.
	 * @return A list of all query parts to be executed locally.
	 */
	private static List<QueryPart> distributeParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID,
			QueryBuildConfiguration parameters) {

		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must be not null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must be not null!");
		Preconditions.checkNotNull(parameters, "transCfg must be not null!");

		// The query parts to be executed locally
		final List<QueryPart> localParts = Lists.newArrayList();

		// The ID of the local peer
		final PeerID ownPeerID = P2PNetworkManagerService.get().getLocalPeerID();

		for(final QueryPart part : queryPartDistributionMap.keySet()) {

			// The ID of the assigned peer for that query part
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);

			if(assignedPeerID.equals(ownPeerID)) {

				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);

			} else {

				SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
				LOG.debug("Plan of the querypart to publish:\n{}", printer.createString(part.getLogicalPlan()));
				DistributionHelper.publish(part, assignedPeerID, sharedQueryID, parameters);

			}

		}

		return localParts;

	}

	public static Collection<ILogicalOperator> collectOperators(ILogicalOperator currentOperator) {
		List<ILogicalOperator> result = Lists.newArrayList();
		collectOperatorsImpl(currentOperator, result);
		return result;
	}
	
	private static void collectOperatorsImpl(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator)) {

			list.add(currentOperator);

			for (final LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperatorsImpl(subscription.getTarget(), list);
			}
		}
	}

	public static void removeTopAOs(ILogicalOperator plan) {
		Collection<ILogicalOperator> operators = collectOperators(plan);

		for( ILogicalOperator operator : operators ) {
			if( operator instanceof TopAO ) {
				// top-most-operator must stay
				if( operator.getSubscriptions().isEmpty() ) {
					continue;
				}

				Map<ILogicalOperator, Integer> sources = Maps.newHashMap();
				Map<ILogicalOperator, Integer> sinks = Maps.newHashMap();

				for( LogicalSubscription subToSink : operator.getSubscriptions().toArray(new LogicalSubscription[0]) ) {
					sinks.put(subToSink.getTarget(), subToSink.getSinkInPort());
					operator.unsubscribeSink(subToSink);
				}

				for( LogicalSubscription subToSource : operator.getSubscribedToSource().toArray(new LogicalSubscription[0]) ) {
					sources.put(subToSource.getTarget(), subToSource.getSourceOutPort());
					operator.unsubscribeFromSource(subToSource);
				}

				for( ILogicalOperator source : sources.keySet() ) {
					for( ILogicalOperator sink : sinks.keySet() ) {
						source.subscribeSink(sink, sinks.get(sink), sources.get(source), source.getOutputSchema());
					}
				}
			}
		}
	}

}