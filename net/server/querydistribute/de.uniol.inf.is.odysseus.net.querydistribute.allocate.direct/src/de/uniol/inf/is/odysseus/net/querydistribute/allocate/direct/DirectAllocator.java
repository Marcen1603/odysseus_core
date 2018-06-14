package de.uniol.inf.is.odysseus.net.querydistribute.allocate.direct;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.net.querydistribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.net.querydistribute.QueryPartAllocationException;

public class DirectAllocator implements IQueryPartAllocator {

	private static final long serialVersionUID = 4368655195925748979L;

	@Override
	public String getName() {
		return "direct";
	}

	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<IOdysseusNode> knownRemotePeers, IOdysseusNode localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {

		if (allocatorParameters.size() != 1) {
			throw new QueryPartAllocationException("Direct allocation needs exactly one parameter instead of " + allocatorParameters.size() + "!");
		}

		IOdysseusNode peerID = determinePeerID(localPeerID, knownRemotePeers, allocatorParameters.get(0));

		Map<ILogicalQueryPart, IOdysseusNode> resultMap = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : queryParts) {
			resultMap.put(queryPart, peerID);
		}
		return resultMap;
	}

	private static IOdysseusNode determinePeerID(IOdysseusNode localPeerID, Collection<IOdysseusNode> remoteNodes, String peerName) throws QueryPartAllocationException {
		if (peerName.equalsIgnoreCase("local")) {
			return localPeerID;
		}
		if( localPeerID.getName().equals(peerName)) {
			return localPeerID;
		}

		for( IOdysseusNode node : remoteNodes ) {
			if( node.getName().equals(peerName)) {
				return node;
			}
		}
		
		throw new QueryPartAllocationException("Peer '" + peerName + "' is not known!");
	}
	
	@Override
	public Map<ILogicalQueryPart, IOdysseusNode> reallocate(Map<ILogicalQueryPart, IOdysseusNode> previousAllocationMap, Collection<IOdysseusNode> faultyPeers, Collection<IOdysseusNode> knownRemotePeers, IOdysseusNode localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		throw new QueryPartAllocationException("Reallocation with direct allocator not supported!");
	}

}
