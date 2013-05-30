package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.discovery.DiscoveryService;
import net.jxta.document.AdvertisementFactory;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.pipe.PipeID;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPartAdvertisement;
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
	 * Generates a {@link RenameAO} logical operator as a placeholder. <br />
	 * The {@link RenameAO} will be subscribed to all real sinks of the {@link QueryPart}, but it won't be part of the {@link QueryPart}.
	 * @param queryPart The {@link QueryPart}.
	 * @return The new {@link RenameAO} with <code>NoOp</code> set.
	 */
	public static RenameAO generateRenameAO(QueryPart queryPart) {
		
		final RenameAO renameAO = new RenameAO();
		renameAO.setNoOp(true);
		renameAO.addParameterInfo("isNoOp", "'true'");
		Collection<ILogicalOperator> sinks = queryPart.getRealSinks();
		int sinkInPort = 0;
		for(final ILogicalOperator sink : sinks)
			renameAO.subscribeToSource(sink, sinkInPort++, 0, sink.getOutputSchema());
		return renameAO;
		
	}
	
	/**
	 * Replaces every {@link StreamAO} within a {@link QueryPart} by its logical subplan.
	 * @param part The {@link QueryPart} where the {@linkStreamAO}s shall be replaced.
	 * @param user The {@link ISession} instance to identify the user who wants this replacement.
	 * @param dictionary The {@link IDataDictionary} to get the logical subplan of a {@link StreamAO}.
	 * @return The new {@link QueryPlan} without the {@link StreamAO}.
	 */
	public static QueryPart replaceStreamAOs(QueryPart part, ISession user, IDataDictionary dictionary) {
		
		final List<ILogicalOperator> operators = Lists.newArrayList();
		for(ILogicalOperator operator : part.getOperators())
			operators.add(operator);
		
		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();
		final List<ILogicalOperator> operatorsToAdd = Lists.newArrayList();
		
		for(ILogicalOperator operator : operators) {
			
			if(operator instanceof StreamAO) {
				
				ILogicalOperator streamPlan = dictionary.getStreamForTransformation(((StreamAO) operator).getStreamname(), user);
				RestructHelper.replaceWithSubplan(operator, streamPlan);
				operatorsToRemove.add(operator);
				operatorsToAdd.add(streamPlan);
				
			}
			
		}
		
		for(ILogicalOperator operator : operatorsToRemove)
			operators.remove(operator);
		for(ILogicalOperator operator : operatorsToAdd)
			operators.add(operator);
		
		if(part.getDestinationName().isPresent())
			return new QueryPart(operators, part.getDestinationName().get());
		else return new QueryPart(operators);
		
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
	 * @param localPeerGroupID The ID of the local peer group.
	 */
	public static void generatePeerConnections(Map<QueryPart, PeerID> queryPartDistributionMap, String baseAccessName, String baseSenderName, 
			PeerGroupID localPeerGroupID) {
		
		int connectionNo = 0;
		
		for(final QueryPart queryPart : queryPartDistributionMap.keySet()) {

			for(final ILogicalOperator relativeSink : queryPart.getRelativeSinks()) {
				
				final Map<QueryPart, ILogicalOperator> nextOperators = 
						DistributionHelper.determineNextQueryParts(relativeSink, queryPart, queryPartDistributionMap.keySet());
				if(!nextOperators.isEmpty()) {
					
					for (final QueryPart destQueryPart : nextOperators.keySet()) {
						
						DistributionHelper.generatePeerConnection(destQueryPart, queryPart, relativeSink, nextOperators.get(destQueryPart), 
								baseSenderName + connectionNo, baseAccessName + connectionNo, localPeerGroupID);
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
	 * @param localPeerGroupID The ID of the local peer group.
	 */
	public static void generatePeerConnection(QueryPart senderPart, QueryPart acceptorPart, ILogicalOperator sinkOfSender, 
			ILogicalOperator sourceOfAcceptor, String senderName, String accessName, PeerGroupID localPeerGroupID) {
		
		final PipeID pipeID = IDFactory.newPipeID(localPeerGroupID);

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
	 * @param localPeerGroupID The ID of the local peer group.
	 * @return The generated {@link ID}.
	 */
	public static ID generateSharedQueryID(PeerGroupID localPeerGroupID) {
		
		return IDFactory.newContentID(localPeerGroupID, false, String.valueOf(System.currentTimeMillis()).getBytes());
		
	}
	
	/**
	 * Publishes a {@link QueryPart} on a peer. The {@link QueryPart} will be stored on that peer. 
	 * @see net.jxta.discovery.DiscoveryService#publish(net.jxta.document.Advertisement, long, long)
	 * @param pqlStatement The PQL statement representing the {@link QueryPart} which shall be published.
	 * @param destinationPeerID The ID of the peer where the {@link QueryPart} shall be published.
	 * @param localPeerGroupID The ID of the local peer group.
	 * @param sharedQueryID The {@link ID} for query sharing.
	 * @param transCfgName The name of the transport configuration.
	 * @param discoveryService The asynchronous mechanism for discovering services.
	 */
	public static void publish(String pqlStatement, PeerID destinationPeerID, PeerGroupID localPeerGroupID, ID sharedQueryID, String transCfgName, 
			DiscoveryService discoveryService) {
		
		Preconditions.checkNotNull(pqlStatement, "PQL Statement to share must not be null!");

		// Create a new advertisement
		final QueryPartAdvertisement adv = 
				(QueryPartAdvertisement) AdvertisementFactory.newAdvertisement(QueryPartAdvertisement.getAdvertisementType());
		adv.setID(IDFactory.newPipeID(localPeerGroupID));
		adv.setPeerID(destinationPeerID);
		adv.setPqlStatement(pqlStatement);
		adv.setSharedQueryID(sharedQueryID);
		adv.setTransCfgName(transCfgName);

		// Publish query part
		try {
			
			discoveryService.publish(adv, 10000, 10000);
			LOG.debug("QueryPart {} published", pqlStatement);	
			
		} catch(final IOException ex) {
			
			LOG.error("Could not publish query part", ex);
			
		}
		
	}

}