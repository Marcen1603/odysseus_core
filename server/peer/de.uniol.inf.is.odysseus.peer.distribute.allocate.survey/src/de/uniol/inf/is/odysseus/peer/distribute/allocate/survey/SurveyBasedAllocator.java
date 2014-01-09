package de.uniol.inf.is.odysseus.peer.distribute.allocate.survey;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.distribute.util.LogicalQueryHelper;

public class SurveyBasedAllocator implements IQueryPartAllocator {

	private static IP2PNetworkManager p2pNetworkManager;

	// called by OSGi-DS
	public static void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
	}

	// called by OSGi-DS
	public static void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			p2pNetworkManager = null;
		}
	}
	
	@Override
	public String getName() {
		return "survey";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		// copy --> original. Needed since allocator inserts dummyAOs
		Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap = LogicalQueryHelper.copyQueryPartsDeep(queryParts);

		Map<ILogicalQueryPart, PeerID> allocationMap = SurveyBasedAllocatorImpl.allocate(queryPartsCopyMap.keySet(), config);
		Map<ILogicalQueryPart, PeerID> allocationMapParts = transformToOriginalLogicalQueryParts(allocationMap, queryPartsCopyMap);
		
		return allocationMapParts;
	}

	private static Map<ILogicalQueryPart, PeerID> transformToOriginalLogicalQueryParts(Map<ILogicalQueryPart, PeerID> allocationMapPeerID, Map<ILogicalQueryPart, ILogicalQueryPart> queryPartsCopyMap) {
		Map<ILogicalQueryPart, PeerID> partMap = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPartCopy : allocationMapPeerID.keySet() ) {
			ILogicalQueryPart originalQueryPart = queryPartsCopyMap.get(queryPartCopy);
			partMap.put(originalQueryPart, allocationMapPeerID.get(queryPartCopy));
		}
		
		return partMap;
	}
}
