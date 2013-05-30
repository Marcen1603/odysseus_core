package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.p2p_new.distribute.DistributionHelper;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.PQLGeneratorService;

// TODO javaDoc
// TODO SessionManagementService

/**
 * The <code>InterqueryDistributor</code> distributes different {@link ILogicalQuery}s to peers. So the execution can be done in parallel. <br />
 * The {@link ILogicalQuery}s are assigned to a peer via round robin without the local peer. Nothing will be executed local.
 * @author Michael Brand
 */
public class InterqueryLoadBalancer extends AbstractLoadBalancer {

	private static final Logger LOG = LoggerFactory.getLogger(InterqueryLoadBalancer.class);

	@Override
	public String getName() {
		
		return "interquery";
		
	}

	@Override
	public List<ILogicalQuery> distributeLogicalQueries(IExecutor sender,
			List<ILogicalQuery> queriesToDistribute, String cfgName) {

		if(queriesToDistribute == null || queriesToDistribute.isEmpty()) {
			
			// Nothing to distribute
			return queriesToDistribute;
			
		}
		
		// Get all available peers
		final Collection<PeerID> remotePeerIDs = P2PDictionaryService.get().getRemotePeerIDs();
		if(remotePeerIDs.isEmpty()) {
			
			LOG.debug("Could not find any remote peers to distribute logical query. Executing all locally.");
			return queriesToDistribute;
			
		} else this.logPeerStatus(remotePeerIDs);
		
		// List of all queryparts over all queries
		final List<QueryPart> allQueryParts = Lists.newArrayList();
		final Map<ILogicalQuery, List<QueryPart>> queryPartsMap = Maps.newHashMap();
		
		// List of sessions over all queryparts
		final Map<QueryPart, ISession> sessionMap = Maps.newHashMap();

		for(final ILogicalQuery query : queriesToDistribute) {
			
			// Get all logical operators of the query and remove the TopAOs
			final List<ILogicalOperator> operators = Lists.newArrayList();
			RestructHelper.collectOperators(query.getLogicalPlan(), operators);
			RestructHelper.removeTopAOs(operators);
			
			// Split the query into parts
			final List<QueryPart> queryParts = this.determineQueryParts(operators);
			LOG.debug("Got {} parts of logical query {}", queryParts.size(), query);
			
			// Generate a new logical operator which marks that the query result shall return to this instance
			// TODO
			final List<QueryPart> localParts = Lists.newArrayList();
			for(QueryPart part : queryParts) {
			
				ILogicalOperator localPart = DistributionHelper.generateRenameAO(part);
				localParts.add(new QueryPart(Lists.newArrayList(localPart), AbstractLoadBalancer.getLocalDestinationName()));
				
			}
			
			queryParts.addAll(localParts);
			allQueryParts.addAll(queryParts);
			queryPartsMap.put(query, queryParts);
			
			for(QueryPart part : queryParts)
				sessionMap.put(part, query.getUser());
			
		}
		
		// Assign query parts to peers
		final Map<QueryPart, PeerID> queryPartDistributionMap = 
				this.assignQueryParts(remotePeerIDs, P2PDictionaryService.get().getLocalPeerID(), allQueryParts);
		PeerGroupID localPeerGroupID = P2PDictionaryService.get().getLocalPeerGroupID();
		DistributionHelper.generatePeerConnections(queryPartDistributionMap, getAccessName(), getSenderName(), localPeerGroupID);
		
		final List<ILogicalQuery> localQueries = Lists.newArrayList();
		
		for(ILogicalQuery query : queryPartsMap.keySet()) {
		
			// Generate an ID for the shared query
			final ID sharedQueryID = DistributionHelper.generateSharedQueryID(localPeerGroupID);
			
			// Get all queryParts of this query mapped with the executing peer
			final Map<QueryPart, PeerID> qPDMap = Maps.newHashMap();	// qPD = queryPartDistribution
			for(QueryPart part : queryPartsMap.get(query))
				qPDMap.put(part, queryPartDistributionMap.get(part));
			
			// publish the queryparts
			localQueries.add(DistributionHelper.transformToQuery(this.shareParts(qPDMap, sharedQueryID, cfgName, sessionMap), 
					PQLGeneratorService.get(), query.toString()));
		
		}
		
		// TODO registerAsMaster
		
		return localQueries;
		
	}
	
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		return Lists.newArrayList(new QueryPart(operators));
		
	}
	
	@Override
	protected Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, 
			PeerID localPeerID, List<QueryPart> queryParts) {
		
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		int peerCounter = 0;
		
		for(final QueryPart part : queryParts) {
			
			PeerID peerID = ((List<PeerID>) remotePeerIDs).get(peerCounter);
			Optional<String> peerName = P2PDictionaryService.get().getPeerRemoteName(peerID);
			
			if(part.getDestinationName().isPresent() && part.getDestinationName().get().equals(AbstractLoadBalancer.getLocalDestinationName())) {
				
				// Local part
				distributed.put(part, localPeerID);
				
			} else {
				
				// Skip local peer for distributed part
				while(peerID == localPeerID) {
					
					// Round-Robin
					peerCounter = (peerCounter++) % remotePeerIDs.size();
					
				}
				
				distributed.put(part, peerID);
				
			}			
			
			if(peerName.isPresent())
				LOG.debug("Assign query part {} to peer {}", part, peerName.get());
			else LOG.debug("Assign query part {} to peer {}", part, peerID);
			
		}

		return distributed;
		
	}

}