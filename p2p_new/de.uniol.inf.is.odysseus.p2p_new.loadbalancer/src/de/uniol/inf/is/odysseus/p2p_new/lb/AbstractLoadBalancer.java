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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.distribute.user.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.user.QueryPartAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

// TODO javaDoc
public abstract class AbstractLoadBalancer implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLoadBalancer.class);
	
	private static final String ACCESS_NAME = "JxtaReceiver_";
	
	private static final String SENDER_NAME = "JxtaSender_";
	
	private int numConnections = 0;
	
	protected SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
	
	/**
	 * Returns the identifier of the local peer.
	 */
	protected static String getLocalDestinationName() {
		
		return "local";
		
	}

	/**
	 * Returns the name of this distributor. It can be used as a parameter in a keyword to use this distributor.
	 */
	@Override
	public abstract String getName();

	/**
	 * @return An empty list, because nothing will be executed local.
	 */
	@Override
	public abstract List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queriesToDistribute, String cfgName);
	
	/**
	 * Splits an {@link ILogicalQuery} into a list of {@link QueryPart}s.
	 * @param operators The list of {@link ILogicalOperator}s representing the{@link ILogicalQuery}.
	 * @return The list of {@link QueryPart}s.
	 */
	protected abstract List<QueryPart> determineQueryParts(List<ILogicalOperator> operators);
	
	/**
	 * Maps each {@link QueryPart} to a peer except the local peer via round robin.
	 * @param remotePeerIDs The collection of all peer IDs.
	 * @param localPeerID The ID of the local peer.
	 * @param queryParts The list of all {@link QueryPart}s.
	 * @return The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} shall be stored.
	 */
	protected abstract Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, 
			PeerID localPeerID, List<QueryPart> queryParts);
	
	/**
	 * Logs the number of available peers and their IDs.
	 * @param peerIDs The collection of all peer IDs.
	 */
	protected void logPeerStatus(Collection<PeerID> peerIDs) {
		
		if(LOG.isDebugEnabled()) {
			
			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());
			for(final PeerID peerID : peerIDs) {
				
				Optional<String> peerName = P2PDictionaryService.get().getPeerRemoteName(peerID);
				
				if(peerName.isPresent())
					LOG.debug("\tPeer: {}", peerName.get());
				else LOG.debug("\tPeer:{}", peerID);
				
			}
			
		}
		
	}
	
	/**
	 * Collects recursive all {@link ILogicalOperator}s representing an <code>ILogicalQuery</code>. <br />
	 * This method should be called with {@link ILogicalQuery#getLogicalPlan()} as <code>currentOperator</code>.
	 * @param currentOperator The <code>IlogicalOperator</code> to collect next.
	 * @param list The list of all <code>ILogicalOperators</code> collected so far.
	 */
	protected void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		
		if(!list.contains(currentOperator)) {

			list.add(currentOperator);

			for(final LogicalSubscription subscription : currentOperator.getSubscriptions())
				this.collectOperators(subscription.getTarget(), list);

			for(final LogicalSubscription subscription : currentOperator.getSubscribedToSource())
				this.collectOperators(subscription.getTarget(), list);
			
		}
		
	}
	
	/**
	 * Removes all {@link TopAO} logical operators from a list of {@link ILogicalOperator}s representing an {@link ILogicalQuery}.
	 * @param operators The list of {@link ILogicalOperator}s representing an {@link ILogicalQuery}.
	 */
	protected void removeTopAO(List<ILogicalOperator> operators) {
		
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
	
	protected RenameAO generateRenameAO(QueryPart queryPart) {
		
		final RenameAO renameAO = new RenameAO();
		renameAO.setNoOp(true);
		renameAO.addParameterInfo("isNoOp", "'true'");
		Collection<ILogicalOperator> sinks = queryPart.getRealSinks();
		int sinkInPort = 0;
		for(final ILogicalOperator sink : sinks)
			renameAO.subscribeToSource(sink, sinkInPort++, 0, sink.getOutputSchema());
		return renameAO;
		
	}
	
	protected void insertSenderAndAccess(Map<QueryPart, PeerID> queryPartDistributionMap) {
		
		for(final QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				
				final Map<QueryPart, ILogicalOperator> nextOperators = 
						this.determineNextQueryParts(queryPart, relativeSink, queryPartDistributionMap);
				if(!nextOperators.isEmpty()) {
					
					for (final QueryPart destQueryPart : nextOperators.keySet())
						this.generatePeerConnection(relativeSink, queryPart, nextOperators.get(destQueryPart), destQueryPart);

				}
			}

		}
		
	}
	
	protected Map<QueryPart, ILogicalOperator> determineNextQueryParts(QueryPart currentQueryPart, ILogicalOperator relativeSink, 
			Map<QueryPart, PeerID> queryPartDistributionMap) {
		
		final Map<QueryPart, ILogicalOperator> next = Maps.newHashMap();
		
		if(relativeSink.getSubscriptions().size() > 0) {

			for(final LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				
				final ILogicalOperator target = subscription.getTarget();
				if(!currentQueryPart.getOperators().contains(target))
					next.put(this.findLogicalOperator(target, queryPartDistributionMap.keySet()), target);
				
			}
			
		}
		
		return next;
		
	}
	
	protected QueryPart findLogicalOperator(ILogicalOperator target, Set<QueryPart> parts) {
		
		for(final QueryPart part : parts) {
			
			if(part.containsRelativeSource(target))
				return part;
			
		}

		throw new IllegalArgumentException("Could not find query part for logical operator " + target);
		
	}
	
	protected void generatePeerConnection(ILogicalOperator startOperator, QueryPart startPart, ILogicalOperator endOperator, 
			QueryPart endPart) {
		
		final PipeID pipeID = IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID());

		final JxtaReceiverAO access = new JxtaReceiverAO();
		access.setPipeID(pipeID.toString());
		access.setOutputSchema(this.generateOutputSchema(ACCESS_NAME + numConnections, startOperator.getOutputSchema()));
		access.setSchema(startOperator.getOutputSchema().getAttributes());
		access.setName(ACCESS_NAME + numConnections);

		final JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPipeID(pipeID.toString());
		sender.setName(SENDER_NAME + numConnections);

		final LogicalSubscription removingSubscription = this.determineSubscription(startOperator, endOperator);
		startOperator.unsubscribeSink(removingSubscription);

		startOperator.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), startOperator.getOutputSchema());
		endOperator.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		startPart.addSenderAO(sender, startOperator);
		endPart.addAccessAO(access, endOperator);
		
		numConnections++;
		
	}
	
	protected SDFSchema generateOutputSchema(String basename, SDFSchema outputSchema) {
		
		List<SDFAttribute> attributes = Lists.newArrayList();
		
		for(SDFAttribute attribute : outputSchema)
			attributes.add(new SDFAttribute(basename, attribute.getAttributeName(), attribute));
		
		return new SDFSchema(basename, attributes);
		
	}
	
	protected LogicalSubscription determineSubscription(ILogicalOperator startOperator, ILogicalOperator endOperator) {
		
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
	protected ID generateSharedQueryID() {
		
		return IDFactory.newContentID(P2PDictionaryService.get().getLocalPeerGroupID(), false, 
				String.valueOf(System.currentTimeMillis()).getBytes());
		
	}
	
	/**
	 * Determines which <code>QueryPart</code>s shall be executed locally and publishes the rest on the mapped peers.
	 * @see InterqueryLoadBalancer#publish(QueryPart, String, ID, String)
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s and the IDs of the the peers, where each {@link QueryPart} 
	 * shall be stored.
	 * @param sharedQueryID The {@link ID} for query sharing.
	 * @param transCfgName The name of the transport configuration.
	 */
	protected List<QueryPart> shareParts(Map<QueryPart, PeerID> queryPartDistributionMap, ID sharedQueryID, String transCfgName, 
			Map<QueryPart, ISession> sessionMap) {
		
		final List<QueryPart> localParts = Lists.newArrayList();
		final PeerID ownPeerID = P2PDictionaryService.get().getLocalPeerID();

		for(final QueryPart part : queryPartDistributionMap.keySet()) {
			
			final PeerID assignedPeerID = queryPartDistributionMap.get(part);
			if(assignedPeerID.equals(ownPeerID)) {
				
				localParts.add(part);
				LOG.debug("QueryPart {} locally stored", part);
				
			} else {
				
				QueryPart partToPublish = this.replaceStreamAOs(part, sessionMap.get(part));
				LOG.debug("Plan of the querypart to publish: {}", this.printer.createString(partToPublish.getOperators().iterator().next()));
				publish(partToPublish, assignedPeerID, sharedQueryID, transCfgName);
				
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
	protected void publish(QueryPart part, PeerID destinationPeerID, ID sharedQueryID, String transCfgName) {
		
		Preconditions.checkNotNull(part, "QueryPart to share must not be null!");
		part.removeDestinationName();

		// Get a new advertisement
		final QueryPartAdvertisement adv = 
				(QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID()));
		adv.setPeerID(destinationPeerID);
		adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(part.getOperators().iterator().next()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfgName);

		// Publish query part
		try {
			
			JxtaServicesProviderService.get().getDiscoveryService().publish(adv, 10000, 10000);
			LOG.debug("QueryPart {} published", part);	
			
		} catch(final IOException ex) {
			
			LOG.error("Could not publish query part", ex);
			
		}
		
	}
	
	/**
	 * Collects all real sinks from a list of {@link QueryPart}s.
	 * @param queryParts The list of {@link QueryPart}s from which the real sinks shall be collected.
	 * @return The collection of all real sinks.	
	 */
	protected Collection<ILogicalOperator> collectSinks(List<QueryPart> queryParts) {
		
		final Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for(QueryPart queryPart : queryParts)
			sinks.addAll(queryPart.getRealSinks());
		
		return sinks;
		
	}
	
	/**
	 * Replaces every {@link StreamAO} within a {@link QueryPart} by its logical subplan.
	 * @param part The {@link QueryPart} where the {@linkStreamAO}s shall be replaced.
	 * @param user The {@link ISession} instance to identify the user who wants this replacement.
	 * @return The 
	 */
	protected QueryPart replaceStreamAOs(QueryPart part, ISession user) {
		
		List<ILogicalOperator> operators = Lists.newArrayList();
		for(ILogicalOperator operator : part.getOperators())
			operators.add(operator);
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof StreamAO) {
				
				ILogicalOperator streamPlan = DataDictionaryService.get().getStreamForTransformation(((StreamAO) operator).getStreamname(), user);
				RestructHelper.replaceWithSubplan(operator, streamPlan);
				
			}
			
		}
		
		if(part.getDestinationName().isPresent())
			return new QueryPart(operators, part.getDestinationName().get());
		else return new QueryPart(operators);
		
	}

}