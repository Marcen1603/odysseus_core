package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.PQLGeneratorService;
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
	public static String getParserID() {
		
		return "PQL";
		
	}
	
	/**
	 * Logs the number of available peers and their IDs.
	 * @see Logger#debug(String)
	 * @param peerIDs The collection of all peer IDs.
	 */
	public static void logPeerStatus(Collection<PeerID> peerIDs) {
		
		if(LOG.isDebugEnabled()) {
			
			LOG.debug("Found {} peers to distribute the queries", peerIDs.size());
			for(final PeerID peerID : peerIDs)
				LOG.debug("\tPeer:{}", peerID);
			
		}
		
	}
	
	/**
	 * Collects all real sinks from a list of {@link QueryPart}s.
	 * @param queryParts The list of {@link QueryPart}s from which the real sinks shall be collected.
	 * @return The collection of all real sinks.	
	 */
	public static Collection<ILogicalOperator> collectSinks(List<QueryPart> queryParts) {
		
		final Collection<ILogicalOperator> sinks = Lists.newArrayList();
		for(QueryPart queryPart : queryParts)
			sinks.addAll(queryPart.getRealSinks());
		
		return sinks;
		
	}
	
	/**
	 * Replaces every {@link StreamAO} within a {@link QueryPart} by its logical subplan.
	 * @param part The {@link QueryPart} where the {@linkStreamAO}s shall be replaced.
	 * @return The new {@link QueryPlan} without the {@link StreamAO}.
	 */
	public static QueryPart replaceStreamAOs(QueryPart part) {
		
		final List<ILogicalOperator> operators = Lists.newArrayList();
		for(ILogicalOperator operator : part.getOperators())
			operators.add(operator);
		
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		final List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof StreamAO) {
				
				ILogicalOperator streamPlan = DataDictionaryService.get().getStreamForTransformation(((StreamAO) operator).getStreamname(), 
						SessionManagementService.getActiveSession());
				RestructHelper.replaceWithSubplan(operator, streamPlan);
				operatorsToRemove.add(operator);
				operatorsToAdd.add(streamPlan);
				
			}
			
		}
		
		for(ILogicalOperator operator : operatorsToRemove)
			operators.remove(operator);
		for(ILogicalOperator operator : operatorsToAdd)
			operators.add(operator);
		
		if(part.getDestinationName().isPresent()) {
			return new QueryPart(operators, part.getDestinationName().get());
		}
		
		return new QueryPart(operators);
	}
	
	/**
	 * Determines the {@link QueryPart}s containing {@link ILogicalOperator}s which are subscribed to a given {@link ILogicalOperator}. <br />
	 * {@link ILogicalOperator}s within the same {@link QueryPart} are ignored.
	 * @param relativeSink The {@link ILogicalOperator} whose next {@link ILogicalOperator}s are sought-after.
	 * @param currentQueryPart The {@link QueryPart} containing <code>relativeSink</code>.
	 * @param queryParts The set of {@link QueryPart}s.
	 * @return The mapping of the next {@link ILogicalOperator}s an the {@link QueryPart}s containing them.
	 */
	public static Map<QueryPart, ILogicalOperator> determineNextQueryParts(ILogicalOperator relativeSink, QueryPart currentQueryPart, 
			Set<QueryPart> queryParts) {
		
		final Map<QueryPart, ILogicalOperator> next = Maps.newHashMap();
		
		if(relativeSink.getSubscriptions().size() > 0) {

			for(final LogicalSubscription subscription : relativeSink.getSubscriptions()) {
				
				final ILogicalOperator target = subscription.getTarget();
				if(!currentQueryPart.getOperators().contains(target))
					next.put(DistributionHelper.findLogicalOperator(target, queryParts), target);
				
			}
			
		}
		
		return next;
		
	}
	
	/**
	 * Searches for an {@link ILogicalOperator} within a set of {@link QueryPart}s.
	 * @param target The sought-after {@link ILogicalOperator}.
	 * @param parts The set of {@link QueryPart}s.
	 * @return The {@link QueryPart} containing <code>target</code>
	 * @throws IllegalArgumentExcepion if <code>target</code> can not be found.
	 */
	public static QueryPart findLogicalOperator(ILogicalOperator target, Set<QueryPart> parts) throws IllegalArgumentException {
		
		for(final QueryPart part : parts) {
			
			if(part.containsRelativeSource(target))
				return part;
			
		}

		throw new IllegalArgumentException("Could not find query part for logical operator " + target);
		
	}
	
	/**
	 * Generates the needed connections between the peers to distribute a list of {@link QueryPart}s.
	 * @param queryPartDistributionMap The mapping of the {@link QueryPart}s to be distributed and the {@link PeerID}s of the peers, 
	 * where they shall be executed.
	 * @param baseAccessName The base name of all accessing operators to be created. To identify an accessing operator the base name will be 
	 * extended by a connection number.
	 * @param baseSenderName The base name of all sending operators to be created. To identify an sending operator the base name will be 
	 * extended by a connection number.
	 */
	public static void generatePeerConnections(Map<QueryPart, PeerID> queryPartDistributionMap, String baseAccessName, String baseSenderName) {
		
		int connectionNo = 0;
		
		for(final QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				
				final Map<QueryPart, ILogicalOperator> nextOperators = 
						DistributionHelper.determineNextQueryParts(relativeSink, queryPart, queryPartDistributionMap.keySet());
				if(!nextOperators.isEmpty()) {
					
					for (final QueryPart destQueryPart : nextOperators.keySet()) {
						
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
	 * @param senderPart The {@link QueryPart} to be executed by the sender.
	 * @param acceptorPart The {@link QueryPart} to be executed by the acceptor.
	 * @param sinkOfSender The sink of <code/>senderPart</code>.
	 * @param sourceOfAcceptor The source of <code/>acceptorPart</code>.
	 * @param senderName The name of the sending operator to be created.
	 * @param accessName The name of the accessing operator to be created.
	 */
	public static void generatePeerConnection(QueryPart senderPart, QueryPart acceptorPart, ILogicalOperator sinkOfSender, 
			ILogicalOperator sourceOfAcceptor, String senderName, String accessName) {
		
		final PipeID pipeID = IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID());

		final JxtaReceiverAO access = new JxtaReceiverAO();
		access.setPipeID(pipeID.toString());
		access.setOutputSchema(RestructHelper.generateOutputSchema(accessName, sinkOfSender.getOutputSchema()));
		access.setSchema(sinkOfSender.getOutputSchema().getAttributes());
		access.setName(accessName);

		final JxtaSenderAO sender = new JxtaSenderAO();
		sender.setPipeID(pipeID.toString());
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
	 * @param queryPart The {@link QueryPart} which shall be published.
	 * @param destinationPeerID The ID of the peer where the {@link QueryPart} shall be published.
	 * @param sharedQueryID The {@link ID} for query sharing.
	 * @param transCfgName The name of the transport configuration.
	 */
	public static void publish(QueryPart queryPart, PeerID destinationPeerID, ID sharedQueryID, String transCfgName) {

		// Create a new advertisement
		final QueryPartAdvertisement adv = 
				(QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(P2PDictionaryService.get().getLocalPeerGroupID()));
		adv.setPeerID(destinationPeerID);
		adv.setPqlStatement(PQLGeneratorService.get().generatePQLStatement(queryPart.getOperators().iterator().next()));
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfgName);

		// Publish query part
		try {
			
			JxtaServicesProviderService.get().getDiscoveryService().publish(adv, 10000, 10000);
			LOG.debug("QueryPart {} published", queryPart);	
			
		} catch(final IOException ex) {
			
			LOG.error("Could not publish query part", ex);
			
		}
		
	}
	
	/**
	 * Creates a new {@link ILogicalQuery} from a list of {@link QueryPart}s.
	 * @param queryParts The list if {@link QueryPart}s which shall be assembled.
	 * @param name The name of the new {@link ILogicalQuery}.
	 * @return The new {@link ILogicalQuery}.
	 */
	public static ILogicalQuery transformToQuery(List<QueryPart> queryParts, String name) {
		
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

}