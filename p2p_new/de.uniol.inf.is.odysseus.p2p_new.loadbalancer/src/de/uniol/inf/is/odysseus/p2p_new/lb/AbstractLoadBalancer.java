package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.discovery.DiscoveryService;
import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.distribution.ILogicalQueryDistributor;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.DataDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.JxtaServicesProviderService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.PQLGeneratorService;

// TODO javaDoc
public abstract class AbstractLoadBalancer implements ILogicalQueryDistributor {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractLoadBalancer.class);
	
	protected SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
	
	protected static String getAccessName() {
		
		return "JxtaReceiver_";
		
	}

	protected static String getSenderName() {
		
		return "JxtaSender_";
		
	}
	
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
				
				QueryPart partToPublish = DistributionHelper.replaceStreamAOs(part, sessionMap.get(part), DataDictionaryService.get());
				LOG.debug("Plan of the querypart to publish: {}", this.printer.createString(partToPublish.getOperators().iterator().next()));
				String pqlStatement = PQLGeneratorService.get().generatePQLStatement(partToPublish.getOperators().iterator().next());
				PeerGroupID localPeerGroupID = P2PDictionaryService.get().getLocalPeerGroupID();
				DiscoveryService disService = JxtaServicesProviderService.get().getDiscoveryService();
				DistributionHelper.publish(pqlStatement, assignedPeerID, localPeerGroupID, sharedQueryID, transCfgName, disService);
				
			}

		}

		return localParts;
		
	}

}