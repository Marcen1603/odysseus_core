package de.uniol.inf.is.odysseus.peer.distribute;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;

public interface IQueryPartAllocator {

	public String getName();
	public Map<ILogicalQueryPart, PeerID> allocate( Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config ) throws QueryPartAllocationException;
	
}
