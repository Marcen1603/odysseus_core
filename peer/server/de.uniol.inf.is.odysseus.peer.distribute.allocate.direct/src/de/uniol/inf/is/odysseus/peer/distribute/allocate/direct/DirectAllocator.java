package de.uniol.inf.is.odysseus.peer.distribute.allocate.direct;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;

public class DirectAllocator implements IQueryPartAllocator {

	private static IPeerDictionary peerDictionary;

	// called by OSGi-DS
	public static void bindPeerDictionary(IPeerDictionary serv) {
		peerDictionary = serv;
	}

	// called by OSGi-DS
	public static void unbindPeerDictionary(IPeerDictionary serv) {
		if (peerDictionary == serv) {
			peerDictionary = null;
		}
	}

	@Override
	public String getName() {
		return "direct";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters)
			throws QueryPartAllocationException {

		if (allocatorParameters.size() != 1) {
			throw new QueryPartAllocationException("Direct allocation needs exactly one parameter instead of " + allocatorParameters.size() + "!");
		}

		PeerID peerID = determinePeerID(localPeerID, allocatorParameters.get(0));

		Map<ILogicalQueryPart, PeerID> resultMap = Maps.newHashMap();
		for (ILogicalQueryPart queryPart : queryParts) {
			resultMap.put(queryPart, peerID);
		}
		return resultMap;
	}

	private static PeerID determinePeerID(PeerID localPeerID, String peerName) throws QueryPartAllocationException {
		if (peerName.equalsIgnoreCase("local")) {
			return localPeerID;
		}

		Optional<PeerID> optPeerID = determineRemotePeerID(peerName);
		if (!optPeerID.isPresent()) {
			throw new QueryPartAllocationException("Peer '" + peerName + "' is not known!");
		}
		return optPeerID.get();
	}

	private static Optional<PeerID> determineRemotePeerID(String peerName) {
		for (PeerID peerID : peerDictionary.getRemotePeerIDs()) {
			if (peerDictionary.getRemotePeerName(peerID).equals(peerName)) {
				return Optional.of(peerID);
			}
		}
		return Optional.absent();
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(Map<ILogicalQueryPart, PeerID> previousAllocationMap, Collection<PeerID> faultyPeers, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config,
			List<String> allocatorParameters) throws QueryPartAllocationException {
		throw new QueryPartAllocationException("Reallocation with direct allocator not supported!");
	}
}
