package de.uniol.inf.is.odysseus.p2p_new.lb;

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
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IP2PDictionary;
import de.uniol.inf.is.odysseus.p2p_new.distribute.user.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.user.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;

/**
 * The <code>InterqueryDistributor</code> distributes different {@link ILogicalQuery}s to peers. So the execution can be done in parallel. <br />
 * The {@link ILogicalQuery}s are assigned to a peer via round robin without the local peer. Nothing will be executed local.
 * @author Michael Brand
 */
public class InterqueryLoadBalancer implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(InterqueryLoadBalancer.class);
	
	/**
	 * The identifier of the local peer.
	 */
	private static final String LOCAL_DESTINATION_NAME = "local";
	
	// TODO javaDoc
	private static final String ACCESS_NAME = "JxtaReceiver_";
	private static final String SENDER_NAME = "JxtaSender_";
	private static int numConnections = 0;
	
	/**
	 * The name of this distributor. It can be used as a parameter in a keyword to use this distributor.
	 */
	private static final String DISTRIBUTION_TYPE = "interquery";

	@Override
	public String getName() {
		
		return DISTRIBUTION_TYPE;
		
	}
	
	/**
	 * The <code>generator</code> generates PQL statements.
	 */
	private static IPQLGenerator generator;
	
	/**
	 * Binds the {@link IPQLGenerator} to generate PQL statements. <br />
	 * Called by OSGi-DS
	 * @param gen The {@link IPQLGenerator} to be binded.
	 */
	public final void bindPQLGenerator(IPQLGenerator gen) {
		
		generator = gen;
		LOG.debug("PQLGenerator bound {}", gen);
		
	}
	
	/**
	 * Unbinds the {@link IPQLGenerator}. <br />
	 * Called by OSGi-DS
	 * @see #bindPQLGenerator(IPQLGenerator)
	 * @param gen The {@link IPQLGenerator} to be unbinded.
	 */
	public final void unbindPQLGenerator(IPQLGenerator gen) {
		
		if(generator == gen) {
			
			generator = null;
			LOG.debug("PQLGenerator unbound {}", gen);
			
		}
		
	}
	
	/**
	 * The <code>p2pDictionary</code> is used to get access to other peers.
	 */
	private static IP2PDictionary p2pDictionary;
	
	/**
	 * Binds the {@link IP2PDictionary} to get access to other peers. <br />
	 * Called by OSGi-DS
	 * @param dic The {@link IP2PDictionary} to be binded.
	 */
	public final void bindP2PDictionary(IP2PDictionary dic) {
		
		p2pDictionary = dic;
		LOG.debug("P2PDictionary bound {}", dic);
		
	}
	
	/**
	 * Undbinds the {@link IP2PDictionary}. <br />
	 * Called by OSGi-DS
	 * @param dic The {@link IP2PDictionary} to be unbinded.
	 */
	public final void unbindP2PDictionary(IP2PDictionary dic) {
		
		if(p2pDictionary == dic) {
			
			p2pDictionary = null;
			LOG.debug("P2PDictionary unbound {}", dic);
			
		}
		
	}

	/**s
	 * @return An empty list, because nothing will be executed local.
	 */
	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queriesToDistribute, String cfgName) {

		if(queriesToDistribute == null || queriesToDistribute.isEmpty()) {
			
			// Nothing to distribute
			return queriesToDistribute;
			
		}
		
		// Get all available peers
		final Collection<PeerID> remotePeerIDs = p2pDictionary.getRemotePeerIDs();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queriesToDistribute;
			
		} else logPeerStatus(remotePeerIDs);
		
		// List of all queryparts over all queries
		final List<QueryPart> allQueryParts = Lists.newArrayList();
		final Map<ILogicalQuery, List<QueryPart>> queryPartsMap = Maps.newHashMap();

		for(final ILogicalQuery query : queriesToDistribute) {
			
			// Get all logical operators of the query and remove the TopAOs
			final List<ILogicalOperator> operators = Lists.newArrayList();
			collectOperators(query.getLogicalPlan(), operators);
			removeTopAO(operators);
			
			// Split the query into parts
			final List<QueryPart> queryParts = determineQueryParts(operators);
			LOG.debug("Got {} parts of logical query {}", queryParts.size(), query);
			
			// Generate a new logical operator which marks that the query result shall return to this instance
			List<ILogicalOperator> localPart = Lists.newArrayList();
			localPart.add(generateRenameAO(queryParts));
			queryParts.add(new QueryPart(localPart));
			
			allQueryParts.addAll(queryParts);
			queryPartsMap.put(query, queryParts);
			
		}
		
		// Assign query parts to peers
		final Map<QueryPart, PeerID> queryPartDistributionMap = assignQueryParts(remotePeerIDs, p2pDictionary.getLocalPeerID(), allQueryParts);
		insertSenderAndAccess(queryPartDistributionMap);
		
		for(ILogicalQuery query : queryPartsMap.keySet()) {
		
			// Generate an ID for the shared query
			final ID sharedQueryID = generateSharedQueryID();
			
			// Get all queryParts of this query mapped with the executing peer
			final Map<QueryPart, PeerID> qPDMap = Maps.newHashMap();	// qPD = queryPartDistribution
			for(QueryPart part : queryPartsMap.get(query))
				qPDMap.put(part, queryPartDistributionMap.get(part));
			
			// publish the queryparts
			// TODO
//			publish(qPDMap, sharedQueryID, cfgName);
			shareParts(qPDMap, sharedQueryID, cfgName);
		
		}
		
		return Lists.newArrayList();
		
	}
	
	/**
	 * Logs the number of available peers and their IDs.
	 * @param peerIDs The collection of all peer IDs.
	 */
	private static void logPeerStatus(Collection<PeerID> peerIDs) {
		
		if (LOG.isDebugEnabled()) {
			
			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());
			for (final PeerID peerID : peerIDs)
				LOG.debug("\tPeer: {}", peerID);
			
		}
		
	}
	
	/**
	 * Collects recursive all {@link ILogicalOperator}s representing an <code>ILogicalQuery</code>. <br />
	 * This method should be called with {@link ILogicalQuery#getLogicalPlan()} as <code>currentOperator</code>.
	 * @param currentOperator The <code>IlogicalOperator</code> to collect next.
	 * @param list The list of all <code>ILogicalOperators</code> collected so far.
	 */
	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		
		if(!list.contains(currentOperator)) {

			list.add(currentOperator);

			for(final LogicalSubscription subscription : currentOperator.getSubscriptions())
				collectOperators(subscription.getTarget(), list);

			for(final LogicalSubscription subscription : currentOperator.getSubscribedToSource())
				collectOperators(subscription.getTarget(), list);
			
		}
		
	}
	
	/**
	 * Removes all {@link TopAO} logical operators from a list of {@link ILogicalOperator}s representing an {@link ILogicalQuery}.
	 * @param operators The list of {@link ILogicalOperator}s representing an {@link ILogicalQuery}.
	 */
	private static void removeTopAO(List<ILogicalOperator> operators) {
		
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		
		for(final ILogicalOperator operator : operators) {
			
			if(operator instanceof TopAO) {
				
				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);
				
			}
			
		}

		for(final ILogicalOperator operatorToRemove : operatorsToRemove)
			operators.remove(operatorToRemove);
		
	}
	
	// TODO javaDoc
	private static RenameAO generateRenameAO(List<QueryPart> queryParts) {
		
		final RenameAO renameAO = new RenameAO();
		renameAO.setDestinationName(LOCAL_DESTINATION_NAME);
		Collection<ILogicalOperator> sinks = collectSinks(queryParts);
		int sinkInPort = 0;
		for(final ILogicalOperator sink : sinks)
			renameAO.subscribeToSource(sink, sinkInPort++, 0, sink.getOutputSchema());
		return renameAO;
		
	}
	
	// TODO javaDoc
	private static void insertSenderAndAccess(Map<QueryPart, PeerID> queryPartDistributionMap) {
		
		for(final QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				
				final Map<QueryPart, ILogicalOperator> nextOperators = determineNextQueryParts(queryPart, relativeSink, queryPartDistributionMap);
				if(!nextOperators.isEmpty()) {
					
					for (final QueryPart destQueryPart : nextOperators.keySet())
						generatePeerConnection(relativeSink, queryPart, nextOperators.get(destQueryPart), destQueryPart);

				}
			}

		}
		
	}
	
	// TODO javaDoc
	private static Map<QueryPart, ILogicalOperator> determineNextQueryParts(QueryPart currentQueryPart, ILogicalOperator relativeSink, 
			Map<QueryPart, PeerID> queryPartDistributionMap) {
		
		final Map<QueryPart, ILogicalOperator> next = Maps.newHashMap();
		
		if(relativeSink.getSubscriptions().size() > 0) {

			for(final LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				
				final ILogicalOperator target = subscription.getTarget();
				if(!currentQueryPart.getOperators().contains(target))
					next.put(findLogicalOperator(target, queryPartDistributionMap.keySet()), target);
				
			}
			
		}
		
		return next;
		
	}
	
	// TODO javaDoc
	private static QueryPart findLogicalOperator(ILogicalOperator target, Set<QueryPart> parts) {
		
		for(final QueryPart part : parts) {
			
			if(part.containsRelativeSource(target))
				return part;
			
		}

		throw new IllegalArgumentException("Could not find query part for logical operator " + target);
		
	}
	
	// TODO javaDoc
	private static void generatePeerConnection(ILogicalOperator startOperator, QueryPart startPart, ILogicalOperator endOperator, 
			QueryPart endPart) {
		
		final PipeID pipeID = IDFactory.newPipeID(p2pDictionary.getLocalPeerGroupID());

		final JxtaReceiverAO access = new JxtaReceiverAO();
		access.setPipeID(pipeID.toString());
		access.setOutputSchema(generateOutputSchema(ACCESS_NAME + numConnections, startOperator.getOutputSchema()));
		access.setSchema(startOperator.getOutputSchema().getAttributes());
		access.setName(ACCESS_NAME + numConnections);

		final JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPipeID(pipeID.toString());
		sender.setName(SENDER_NAME + numConnections);

		final LogicalSubscription removingSubscription = determineSubscription(startOperator, endOperator);
		startOperator.unsubscribeSink(removingSubscription);

		startOperator.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), startOperator.getOutputSchema());
		endOperator.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		startPart.addSenderAO(sender, startOperator);
		endPart.addAccessAO(access, endOperator);
		
		numConnections++;
		
	}
	
	// TODO javaDoc
	private static SDFSchema generateOutputSchema(String basename, SDFSchema outputSchema) {
		
		List<SDFAttribute> attributes = Lists.newArrayList();
		
		for(SDFAttribute attribute : outputSchema)
			attributes.add(new SDFAttribute(basename, attribute.getAttributeName(), attribute));
		
		return new SDFSchema(basename, attributes);
		
	}
	
	// TODO javaDoc
	private static LogicalSubscription determineSubscription(ILogicalOperator startOperator, ILogicalOperator endOperator) {
		
		for(final LogicalSubscription subscription : startOperator.getSubscriptions()) {
			
			if(subscription.getTarget().equals(endOperator))
				return subscription;

		}
		
		return null;
		
	}
	
	/**
	 * Generates an {@link ID} for query sharing.
	 * @return The generated {@link ID}.
	 */
	private static ID generateSharedQueryID() {
		
		return IDFactory.newContentID(p2pDictionary.getLocalPeerGroupID(), false, 
				String.valueOf(System.currentTimeMillis()).getBytes());
		
	}
	
	/**
	 * Pubslishes a List of {@linkQueryPart}s on the mapped peers.
	 * @see InterqueryLoadBalancer#publish(QueryPart, String, ID, String)
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} 
	 * shall be stored.
	 * @param sharedQueryID The {@link ID} for query sharing.
	 * @param transCfgName The name of the transport configuration.
	 */
	// TODO
//	private static void publish(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, String transCfgName) {
//		
//		for(final QueryPart part : queryPartDistributionMap.keySet())
//			publish(part, queryPartDistributionMap.get(part), sharedQueryID, transCfgName);
//		
//	}
	private static List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, String transCfgName) {
		final List<QueryPart> localParts = Lists.newArrayList();

		final PeerID ownPeerID = p2pDictionary.getLocalPeerID();

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
	
	/**
	 * Publishes a {@link QueryPart} on a peer. The {@link QueryPart} will be stored on that peer. 
	 * @see net.jxta.discovery.DiscoveryService#publish(net.jxta.document.Advertisement, long, long)
	 * @param part The {@link QueryPart} which shall be published.
	 * @param destinationPeerID The ID of the peer where the {@link QueryPart} shall be published.
	 * @param sharedQueryID The {@link ID} for query sharing.
	 * @param transCfgName The name of the transport configuration.
	 */
//	private static void publish(QueryPart part, PeerID destinationPeerID, ID sharedQueryID, String transCfgName) {
//		
//		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
//		part.removeDestinationName();
//
//		// Get a new advertisement
//		final QueryPartAdvertisement adv = 
//				(QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
//		adv.setID(IDFactory.newPipeID(P2PNewPlugIn.getOwnPeerGroup().getPeerGroupID()));
//		adv.setPeerID(destinationPeerID);
//		adv.setPqlStatement(generator.generatePQLStatement(part.getOperators().iterator().next()));
//		adv.setSharedQueryID(sharedQueryID);
//		adv.setTransCfgName(transCfgName);
//
//		// Publish query part
//		try {
//			
//			P2PNewPlugIn.getDiscoveryService().publish(adv, 10000, 10000);
//			LOG.debug("QueryPart {} published", part);	
//			
//		} catch(final IOException ex) {
//			
//			LOG.error("Could not publish query part", ex);
//			
//		}
//		
//	}
	private static void publish(QueryPart part, PeerID destinationPeer, ID sharedQueryID, String transCfgName) {
		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
		part.removeDestinationName();

		final QueryPartAdvertisement adv = (QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(p2pDictionary.getLocalPeerGroupID()));
		adv.setPeerID(destinationPeer);
		adv.setPqlStatement(generator.generatePQLStatement(part.getOperators().iterator().next()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfgName);

		tryPublishImpl(adv);
		LOG.debug("QueryPart {} published", part);
	}
	private static void tryPublishImpl(QueryPartAdvertisement adv) {
		try {
			JxtaServicesProviderService.get().getDiscoveryService().publish(adv, 10000, 10000);
		} catch (final IOException ex) {
			LOG.error("Could not publish query part", ex);
		}
	}
	
	/**
	 * Collects all real sinks from a list of {@link QueryPart}s.
	 * @param queryParts The list of {@link QueryPart}s from which the real sinks shall be collected.
	 * @return The collection of all real sinks.
	 */
	private static Collection<ILogicalOperator> collectSinks(List<QueryPart> queryParts) {
		
		final Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for(QueryPart queryPart : queryParts)
			sinks.addAll(queryPart.getRealSinks());
		
		return sinks;
		
	}
	
	/**
	 * Splits an {@link ILogicalQuery} into a list of {@link QueryPart}s.
	 * @param operators The list of {@link ILogicalOperator}s representing the{@link ILogicalQuery}.
	 * @return The list of {@link QueryPart}s.
	 */
	private static List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		return Lists.newArrayList(new QueryPart(operators));
		
	}
	
	/**
	 * Maps each {@link QueryPart} to a peer except the local peer via round robin.
	 * @param remotePeerIDs The collection of all peer IDs.
	 * @param localPeerID The ID of the local peer.
	 * @param queryParts The list of all {@link QueryPart}s.
	 * @return The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} shall be stored.
	 */
	private static Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, 
			PeerID localPeerID, List<QueryPart> queryParts) {
		
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		final Map<String, PeerID> assignedDestinations = Maps.newHashMap();

		// Get all present instances
		for(final PeerID remotePeerID : remotePeerIDs) {
			
			// Skip local peer
			if(remotePeerID == localPeerID)
				continue;
			
			final Optional<String> optRemotePeerName = p2pDictionary.getPeerRemoteName(remotePeerID);
			if(optRemotePeerName.isPresent())
				assignedDestinations.put(optRemotePeerName.get(), remotePeerID);
			
		}

		// Round-Robin-assignment
		final List<PeerID> remotePeerIDList = Lists.newArrayList(remotePeerIDs);
		int peerCounter = 0;
		for(final QueryPart queryPart : queryParts) {
			final String destinationName = queryPart.getDestinationName().get();

			PeerID assignedPeer = assignedDestinations.get(destinationName);
			if(assignedPeer == null) {
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

}