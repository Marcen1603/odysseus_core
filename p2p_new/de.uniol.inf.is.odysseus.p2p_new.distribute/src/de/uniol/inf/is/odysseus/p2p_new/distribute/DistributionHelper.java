package de.uniol.inf.is.odysseus.p2p_new.distribute;

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

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.PQLGeneratorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.ServerExecutorService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.SessionManagementService;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaReceiverAO;
import de.uniol.inf.is.odysseus.p2p_new.logicaloperator.JxtaSenderAO;

/**
 * A collection of useful tools for distributors.
 * @see ILogicalQueryDistributor
 * @author Michael Brand
 */
public class DistributionHelper {
	
	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(DistributionHelper.class);
	
	/**
	 * Returns the ID for the PQL parser.
	 */
	public static String getPQLParserID() {
		
		return "PQL";
		
	}
	
	/**
	 * Logs the number of available remote peers and their IDs.
	 * @param peerIDs The collection of all remote peer IDs.
	 */
	public static void logPeerStatus(Collection<PeerID> peerIDs) throws NullPointerException {
		
		if(LOG.isDebugEnabled()) {
			
			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());
			for(final PeerID peerID : peerIDs) {
				
				Optional<String> peerName = P2PDictionaryService.get().getRemotePeerName(peerID);
				if(peerName.isPresent())
					LOG.debug("\tPeer: {}", peerName.get());
				else LOG.debug("\tPeer: {}", peerID);
				
			}
			
		}
		
	}
	
	/**
	 * Collects all real sinks from a list of {@link QueryPart}s.
	 * @param queryParts The list of {@link QueryPart}s from which the real sinks shall be collected. <br />
	 * <code>queryParts</code> must not be null.
	 * @return The collection of all real sinks.	
	 */
	public static Collection<ILogicalOperator> collectSinks(List<QueryPart> queryParts) {
		
		Preconditions.checkNotNull(queryParts, "queryParts must not be null!");
		
		final Collection<ILogicalOperator> sinks = Lists.newArrayList();
		
		for(QueryPart queryPart : queryParts)
			sinks.addAll(queryPart.getRealSinks());
		
		return sinks;
		
	}
	
	/**
	 * Replaces every {@link StreamAO} within a {@link QueryPart} by its logical subplan.
	 * @param part The {@link QueryPart} where the {@linkStreamAO}s shall be replaced. <br />
	 * <code>part</code> must not be null.
	 * @return The new {@link QueryPlan} without the {@link StreamAO}.
	 */
	public static QueryPart replaceStreamAOs(QueryPart part) {
		
		Preconditions.checkNotNull(part,"part must not be null!");
		
		final List<ILogicalOperator> operators = Lists.newArrayList();
		for(ILogicalOperator operator : part.getOperators())
			operators.add(operator);
		
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		final List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof StreamAO) {
				
				ILogicalOperator streamPlan = ServerExecutorService.getDataDictionary(SessionManagementService.getActiveSession().getTenant()).getStreamForTransformation(((StreamAO) operator).getStreamname(), 
						SessionManagementService.getActiveSession());
				ILogicalOperator streamPlanCopy = copyLogicalPlan(streamPlan);
				RestructHelper.replaceWithSubplan(operator, streamPlanCopy);
				operatorsToRemove.add(operator);
				operatorsToAdd.add(streamPlanCopy);
				
			}
			
		}
		
		operators.removeAll(operatorsToRemove);
		operators.addAll(operatorsToAdd);
		
		if(part.getDestinationName().isPresent())
			return new QueryPart(operators, part.getDestinationName().get());
		
		return new QueryPart(operators);
		
	}
	
	/**
	 * Copies a logical plan.
	 * @param originPlan The logical plan to be copied. <br />
	 * <code>originPlan</code> must not be null.
	 * @return A copy of <code>originPlan</code>.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ILogicalOperator copyLogicalPlan(ILogicalOperator originPlan) {
		
		Preconditions.checkNotNull(originPlan, "originplan must not be null!");
		
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(originPlan.getOwner());
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(originPlan, copyVisitor);
		return copyVisitor.getResult();
		
	}
	
	/**
	 * Copies an {@link ILogicalQuery}.
	 * @param originQuery The {@link ILogicalQuery} to be copied. <br />
	 * <code>originQuery</code> must not be null and it must have a logical plan.
	 * @see ILogicalQuery#getLogicalPlan()
	 * @return A copy of <code>originQuery</code>.
	 */
	public static ILogicalQuery copyLogicalQuery(ILogicalQuery originQuery) {
		
		Preconditions.checkNotNull(originQuery, "originQuery must not be null!");
		Preconditions.checkNotNull(originQuery.getLogicalPlan(), "originQuery must have a logical plan!");
		
		ILogicalQuery copy = new LogicalQuery(getPQLParserID(), 
				DistributionHelper.copyLogicalPlan(originQuery.getLogicalPlan()),
				originQuery.getPriority());
		copy.setName(originQuery.getName());
		copy.setQueryText(PQLGeneratorService.get().generatePQLStatement(copy.getLogicalPlan()));
		copy.setUser(originQuery.getUser());
		return copy;
		
	}
	
	/**
	 * Determines the {@link QueryPart}s containing {@link ILogicalOperator}s which are subscribed to a given {@link ILogicalOperator}. <br />
	 * {@link ILogicalOperator}s within the same {@link QueryPart} are ignored.
	 * @param relativeSink The {@link ILogicalOperator} whose next {@link ILogicalOperator}s are sought-after.  <br />
	 * <code>relativeSink</code> must not be null.
	 * @param currentQueryPart The {@link QueryPart} containing <code>relativeSink</code>. <br />
	 * <code>currentQueryPart</code> must not be null.
	 * @param queryParts The set of {@link QueryPart}s. <br />
	 * <code>queryParts</code> must not be null.
	 * @return The mapping of the next {@link ILogicalOperator}s an the {@link QueryPart}s containing them.
	 */
	public static Map<QueryPart, ILogicalOperator> determineNextQueryParts(ILogicalOperator relativeSink, QueryPart currentQueryPart, 
			Set<QueryPart> queryParts) {
		
		Preconditions.checkNotNull(relativeSink, "relativeSink must not be null!");
		Preconditions.checkNotNull(currentQueryPart, "currentQueryPart must not be null!");
		Preconditions.checkNotNull(queryParts, "queryParts must not be null!");
		
		final Map<QueryPart, ILogicalOperator> next = Maps.newHashMap();
		
		if(relativeSink.getSubscriptions().size() > 0) {

			for(final LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				
				final ILogicalOperator target = subscription.getTarget();
				if(!currentQueryPart.getOperators().contains(target)) {
					
					QueryPart nextPart = DistributionHelper.findLogicalOperator(target, queryParts);
					if(nextPart != null)
						next.put(nextPart, target);
					
				}
				
			}
			
		}
		
		return next;
		
	}
	
	/**
	 * Searches for an {@link ILogicalOperator} within a set of {@link QueryPart}s.
	 * @param target The sought-after {@link ILogicalOperator}. <br />
	 * <code>target</code> must not be null.
	 * @param parts The set of {@link QueryPart}s. <br />
	 * <code>parts</code> must not be null.
	 * @return The {@link QueryPart} containing <code>target</code> or null, if <code>target</code> can not be found.
	 */
	public static QueryPart findLogicalOperator(ILogicalOperator target, Set<QueryPart> parts) throws IllegalArgumentException {
		
		Preconditions.checkNotNull(target, "target must not be null!");
		Preconditions.checkNotNull(parts, "parts must not be null!");
		
		for(final QueryPart part : parts) {
			
			if(part.containsRelativeSource(target))
				return part;
			
		}

		return null;
		
	}
	
	/**
	 * Generates the needed connections between the peers to distribute a list of {@link QueryPart}s.
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s to be distributed and the {@link PeerID}s of the peers, 
	 * where they shall be executed. <br />
	 * <code>queryPartsDistributionMap</code> must not be null.
	 * @param baseAccessName The base name of all accessing operators to be created. To identify an accessing operator the base name will be 
	 * extended by a connection number. <br />
	 * <code>baseAccessName</code> must not be null.
	 * @param baseSenderName The base name of all sending operators to be created. To identify an sending operator the base name will be 
	 * extended by a connection number. <br />
	 * <code>baseSenderName</code> must not be null.
	 */
	public static void generatePeerConnections(Map<QueryPart, PeerID> queryPartDistributionMap, String baseAccessName, String baseSenderName) {
		
		Preconditions.checkNotNull(queryPartDistributionMap, "queryPartDistributionMap must not be null!");
		Preconditions.checkNotNull(baseAccessName, "baseAccessName must not be null!");
		Preconditions.checkNotNull(baseSenderName, "baseSenderName must not be null!");
		
		int connectionNo = 0;
		
		for(final QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				
				final Map<QueryPart, ILogicalOperator> nextOperators = 
						DistributionHelper.determineNextQueryParts(relativeSink, queryPart, queryPartDistributionMap.keySet());
				if(!nextOperators.isEmpty()) {
					
					for(final QueryPart destQueryPart : nextOperators.keySet()) {
						
						DistributionHelper.generatePeerConnection(queryPart, destQueryPart, relativeSink, nextOperators.get(destQueryPart), 
								baseSenderName + connectionNo, baseAccessName + connectionNo);
						connectionNo++;
						
					}

				}
			}

		}
		
	}
	
	/**
	 * Generates a connection between two Peers, one sender and one acceptor.
	 * @param senderPart The {@link QueryPart} to be executed by the sender. <br />
	 * <code>senderPart</code> must not be null.
	 * @param acceptorPart The {@link QueryPart} to be executed by the acceptor. <br />
	 * <code>acceptorPart</code> must not be null.
	 * @param sinkOfSender The sink of <code/>senderPart</code>. <br />
	 * <code>sinkOfSender</code> must not be null and it must have an output schema.
	 * @see ILogicalOperator#getOutputSchema()
	 * @param sourceOfAcceptor The source of <code/>acceptorPart</code>. <br />
	 * <code>sourceOfAcceptor</code> must not be null.
	 * @param senderName The name of the sending operator to be created.
	 * @param accessName The name of the accessing operator to be created.
	 */
	public static void generatePeerConnection(QueryPart senderPart, QueryPart acceptorPart, ILogicalOperator sinkOfSender, 
			ILogicalOperator sourceOfAcceptor, String senderName, String accessName) {
		
		Preconditions.checkNotNull(senderPart, "senderPart must not be null!");
		Preconditions.checkNotNull(acceptorPart, "acceptorPart must not be null!");
		Preconditions.checkNotNull(sinkOfSender, "sinkOfSender must not be null!");
		Preconditions.checkNotNull(sinkOfSender.getOutputSchema(), "sinkOfSender must have an output schema!");
		Preconditions.checkNotNull(sourceOfAcceptor, "sourceOfAcceptor must not be null!");
		
		final PipeID pipeID = IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID());
		LOG.debug("PipeID {} created", pipeID.toString());

		final JxtaReceiverAO access = new JxtaReceiverAO();
		access.setPipeID(pipeID.toString());
		access.setOutputSchema(sinkOfSender.getOutputSchema());
		access.setSchema(sinkOfSender.getOutputSchema().getAttributes());
		access.setName(accessName);

		final JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPipeID(pipeID.toString());
		sender.setOutputSchema(sinkOfSender.getOutputSchema());
		sender.setName(senderName);

		final LogicalSubscription removingSubscription = RestructHelper.determineSubscription(sinkOfSender, sourceOfAcceptor);
		sinkOfSender.unsubscribeSink(removingSubscription);

		sinkOfSender.subscribeSink(sender, 0, removingSubscription.getSourceOutPort(), sinkOfSender.getOutputSchema());
		sourceOfAcceptor.subscribeToSource(access, removingSubscription.getSinkInPort(), 0, access.getOutputSchema());

		senderPart.addSenderAO(sender, sinkOfSender);
		acceptorPart.addAccessAO(access, sourceOfAcceptor);
		
	}
	
	/**
	 * Generates an {@link ID} for query sharing.
	 * @return The generated {@link ID}.
	 */
	public static ID generateSharedQueryID() {
		
		return IDFactory.newContentID(P2PDictionaryService.get().getLocalPeerGroupID(), false, String.valueOf(System.currentTimeMillis()).getBytes());
		
	}
	
	/**
	 * Publishes a {@link QueryPart} on a peer. The {@link QueryPart} will be stored on that peer. 
	 * @see net.jxta.discovery.DiscoveryService#publish(net.jxta.document.Advertisement, long, long)
	 * @param queryPart The {@link QueryPart} which shall be published. <br />
	 * <code>queryPart</code> must not be null.
	 * @param destinationPeerID The ID of the peer where the {@link QueryPart} shall be published. <br />
	 * <code>destinationPeerID</code> must not be null.
	 * @param sharedQueryID The {@link ID} for query sharing. <br />
	 * <code>sharedQueryID</code> must not be null.
	 * @param transCfg The transport configuration. <br />
	 * <code>trancCfg</code> must not be null.
	 */
	public static void publish(QueryPart queryPart, PeerID destinationPeerID, ID sharedQueryID, QueryBuildConfiguration transCfg) {
		
		Preconditions.checkNotNull(queryPart, "queryPart must not be null!");
		Preconditions.checkNotNull(destinationPeerID, "destinationPeerID must not be null!");
		Preconditions.checkNotNull(sharedQueryID, "sharedQueryID must not be null!");
		Preconditions.checkNotNull(transCfg, "transCfg must not be null!");

		// Create a new advertisement
		final QueryPartAdvertisement adv = 
				(QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID()));
		adv.setPeerID(destinationPeerID);
		adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(queryPart.getLogicalPlan()));
		
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfg.getName());

		JxtaServicesProviderService.get().getDiscoveryService().remotePublish(destinationPeerID.toString(), adv, 15000);
		Optional<String> peerName = P2PDictionaryService.get().getRemotePeerName(destinationPeerID);
		if(peerName.isPresent())
			LOG.debug("QueryPart {} remotely published at {}", queryPart, peerName.get());
		else LOG.debug("QueryPart {} remotely published at {}", queryPart, destinationPeerID);
		
	}
	
	/**
	 * Creates a new {@link ILogicalQuery} from a list of {@link QueryPart}s.
	 * @param queryParts The list if {@link QueryPart}s which shall be assembled. <br />
	 * <code>queryParts</code> must not be null.
	 * @param name The name of the new {@link ILogicalQuery}.
	 * @return The new {@link ILogicalQuery}.
	 */
	public static ILogicalQuery transformToQuery(List<QueryPart> queryParts, String name) {
		
		Preconditions.checkNotNull(queryParts, "queryParts must not be null!");
		
		final Collection<ILogicalOperator> sinks = DistributionHelper.collectSinks(queryParts);
		final TopAO topAO = RestructHelper.generateTopAO(sinks);
	
		final ILogicalQuery logicalQuery = new LogicalQuery();
		logicalQuery.setLogicalPlan(topAO, true);
		logicalQuery.setName(name);
		logicalQuery.setParserId("PQL");
		logicalQuery.setPriority(0);
		logicalQuery.setUser(SessionManagementService.getActiveSession());
		logicalQuery.setQueryText(PQLGeneratorService.get().generatePQLStatement(topAO));
		
		return logicalQuery;
		
	}
	
	/**
	 * Searches and removes all {@link ILogicalOperator}s, which need local resources.
	 * @see ILogicalOperator#needsLocalResources()
	 * @param operators A collection of {@link ILogicalOperator}s.
	 * @return A collection of all removed {@link ILogicalOperator}s. These operators need local resources.
	 */
	public static Collection<ILogicalOperator> filterOperatorsForLocalPart(Collection<ILogicalOperator> operators) {
		
		Preconditions.checkNotNull(operators, "operators must be not null!");
		
		List<ILogicalOperator> operatorsForLocalPart = Lists.newArrayList();
		for(ILogicalOperator operator : operators) {
			
			if(operator.needsLocalResources())
				operatorsForLocalPart.add(operator);
			
		}
		
		operators.removeAll(operatorsForLocalPart);
		return operatorsForLocalPart;
		
	}
	
	/**
	 * Returns the name of the source first found (depth-first-search).
	 */	
	public static Optional<String> getSourceName(ILogicalOperator operator) {
		
		// FIXME: Use Resource instead of String!
		if(operator instanceof StreamAO)
			return Optional.of(((StreamAO) operator).getStreamname().toString());
		else if(operator instanceof AbstractAccessAO)
			return Optional.of(((AbstractAccessAO) operator).getName());
		else {
			
			for(LogicalSubscription subToSource : operator.getSubscribedToSource())
				return DistributionHelper.getSourceName(subToSource.getTarget());
			
		}
		
		return Optional.of(null);
		
	}

}