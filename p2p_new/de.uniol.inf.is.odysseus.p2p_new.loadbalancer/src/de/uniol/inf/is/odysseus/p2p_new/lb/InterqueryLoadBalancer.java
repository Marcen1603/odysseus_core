package de.uniol.inf.is.odysseus.p2p_new.lb;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.distribute.QueryPart;
import de.uniol.inf.is.odysseus.p2p_new.lb.service.P2PDictionaryService;

/**
 * The <code>InterqueryDistributor</code> distributes different {@link ILogicalQuery}s to peers. So the execution can be done in parallel. <br />
 * The {@link ILogicalQuery}s are assigned to a peer via round robin without the local peer. Nothing will be executed local. <br />
 * To use the <code>InterqueryDistributor</code> use the following pre-parser-keywords: <br />
 * #DODISTRIBUTE true <br />
 * #DISTRIBUTIONTYPE interquery
 * @author Michael Brand
 */
public class InterqueryLoadBalancer extends AbstractLoadBalancer {

	/**
	 * The {@link Logger} instance for this class.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(InterqueryLoadBalancer.class);

	@Override
	public String getName() {
		
		return "interquery";
		
	}
	
	/**
	 * Creates one {@link Querypart} for the whole {@link ILogicalQuery}.
	 */
	@Override
	protected List<QueryPart> determineQueryParts(List<ILogicalOperator> operators) {
		
		return Lists.newArrayList(new QueryPart(operators));
		
	}
	
	/**
	 * Assigns all {@link QueryPart}s, which are not assigned to the local peer yet, to the remote peers via round robin.
	 */
	@Override
	protected Map<QueryPart, PeerID> assignQueryParts(Collection<PeerID> remotePeerIDs, 
			PeerID localPeerID, Collection<List<QueryPart>> queryParts) {
		
		final Map<QueryPart, PeerID> distributed = Maps.newHashMap();
		int peerCounter = 0;
		final Iterator<List<QueryPart>> partsIter = queryParts.iterator();
		
		while(partsIter.hasNext()) {
		
			for(final QueryPart part : partsIter.next()) {
			
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
			
		}

		return distributed;
		
	}

}