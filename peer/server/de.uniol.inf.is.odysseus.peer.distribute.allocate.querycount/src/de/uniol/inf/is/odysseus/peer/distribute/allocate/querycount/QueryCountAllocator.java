package de.uniol.inf.is.odysseus.peer.distribute.allocate.querycount;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;

public class QueryCountAllocator implements IQueryPartAllocator {

	@Override
	public String getName() {
		return "QueryCount";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID,
			QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(Map<ILogicalQueryPart, PeerID> previousAllocationMap, Collection<PeerID> faultyPeers, Collection<PeerID> knownRemotePeers, PeerID localPeerID,
			QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		// TODO Auto-generated method stub
		return null;
	}

}
