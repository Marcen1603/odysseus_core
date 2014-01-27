package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;

public abstract class AbstractRoundRobinAllocator implements IQueryPartAllocator {

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		
		List<PeerID> peerIDs = determineConsideredPeerIDs(knownRemotePeers, localPeerID);
		if( peerIDs == null || peerIDs.isEmpty() ) {
			throw new QueryPartAllocationException("There are no peers to be considered for round robin");
		}
		
		int peerIDIndex = 0;
		
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			Collection<PeerID> nonAllowedPeers = determineAvoidedPeers(queryPart, allocationMap);

			int tries = 0;
			int tempPeerIDIndex = peerIDIndex;
			PeerID chosenPeerID = null;
			
			do {
				if( tries >= peerIDs.size() ) {
					// checked all possibilities
					chosenPeerID = null;
					break;
				}
				tempPeerIDIndex = ( tempPeerIDIndex + 1 ) % peerIDs.size();
				chosenPeerID = peerIDs.get(tempPeerIDIndex);
				tries++;
				
			} while( nonAllowedPeers.contains(chosenPeerID) );
			
			if( chosenPeerID == null ) {
				peerIDIndex = ( peerIDIndex + 1 ) % peerIDs.size();
				chosenPeerID = peerIDs.get(peerIDIndex);
			} else {
				peerIDIndex = tempPeerIDIndex;
			}
			
			allocationMap.put(queryPart, chosenPeerID);
		}
		
		return allocationMap;
	}

	private static Collection<PeerID> determineAvoidedPeers(ILogicalQueryPart queryPart, Map<ILogicalQueryPart, PeerID> allocationMap) {
		Collection<PeerID> avoidedPeers = Lists.newArrayList();
		
		for( ILogicalQueryPart avoidedPart : queryPart.getAvoidingQueryParts() ) {
			PeerID avoidedPeerID = allocationMap.get(avoidedPart);
			if( avoidedPeerID != null && !avoidedPeers.contains(avoidedPeerID)) {
				avoidedPeers.add(avoidedPeerID);
			}
		}
		
		return avoidedPeers;
	}

	protected abstract List<PeerID> determineConsideredPeerIDs( Collection<PeerID> knownRemotePeers, PeerID localPeerID );
}
