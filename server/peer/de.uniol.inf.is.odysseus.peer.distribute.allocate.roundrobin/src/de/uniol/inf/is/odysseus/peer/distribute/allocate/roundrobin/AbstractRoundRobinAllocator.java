package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;

public abstract class AbstractRoundRobinAllocator implements IQueryPartAllocator {

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		
		List<PeerID> peerIDs = determineConsideredPeerIDs(knownRemotePeers, localPeerID);
		if( peerIDs == null || peerIDs.isEmpty() ) {
			throw new QueryPartAllocationException("There are no peers to be considered for round robin");
		}
		
		int peerIDIndex = 0;
		
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			allocationMap.put(queryPart, peerIDs.get(peerIDIndex));
			
			peerIDIndex = ( peerIDIndex + 1 ) % peerIDs.size();
		}
		
		return allocationMap;
	}

	protected abstract List<PeerID> determineConsideredPeerIDs( Collection<PeerID> knownRemotePeers, PeerID localPeerID );
}
