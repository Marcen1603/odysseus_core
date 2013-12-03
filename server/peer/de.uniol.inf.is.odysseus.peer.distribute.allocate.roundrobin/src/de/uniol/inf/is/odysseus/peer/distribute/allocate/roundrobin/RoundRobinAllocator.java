package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;

public class RoundRobinAllocator implements IQueryPartAllocator {

	@Override
	public String getName() {
		return "roundrobin";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config) throws QueryPartAllocationException {
		
		List<PeerID> peerIDs = Lists.newArrayList(knownRemotePeers);
		int peerIDIndex = 0;
		
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			allocationMap.put(queryPart, peerIDs.get(peerIDIndex));
			
			peerIDIndex = ( peerIDIndex + 1 ) % peerIDs.size();
		}
		
		return allocationMap;
	}

}
