package de.uniol.inf.is.odysseus.peer.distribute.allocate.querycount;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;

public class QueryCountAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(QueryCountAllocator.class);
	
	private static IPeerResourceUsageManager resourceUsageManager;
	
	public static void bindPeerResourceUsageManager( IPeerResourceUsageManager manager ) {
		resourceUsageManager = manager;
		
		LOG.debug("ResourceUsageManager bound");
	}
	
	public static void unbindPeerResourceUsageManager( IPeerResourceUsageManager manager ) {
		if( manager == resourceUsageManager ) {
			resourceUsageManager = null;
			
			LOG.debug("ResourceUsageManager unbound");
		}
	}
	
	@Override
	public String getName() {
		return "QueryCount";
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		Collection<PeerID> consideredPeerIDs = Lists.newArrayList(knownRemotePeers);
		if( doConsiderLocalPeer(allocatorParameters)) {
			consideredPeerIDs.add(localPeerID);
		}
		
		if( consideredPeerIDs.isEmpty() ) {
			throw new QueryPartAllocationException("There is no peer to allocate to");
		}
		
		PeerQueryCountMap queryCountMap = new PeerQueryCountMap(knownRemotePeers, resourceUsageManager);
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		for( ILogicalQueryPart queryPart : queryParts ) {
			PeerID peerID = queryCountMap.getPeerIDWithLowestQueryCount();
			allocationMap.put(queryPart, peerID);
			
			queryCountMap.incrementQueryCount(peerID);
		}
		
		return allocationMap;
	}
	
	private static boolean doConsiderLocalPeer( List<String> parameters ) {
		for( String parameter : parameters ) {
			if( "withlocal".equalsIgnoreCase(parameter)) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(Map<ILogicalQueryPart, PeerID> previousAllocationMap, Collection<PeerID> faultyPeers, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		// do all again
		return allocate(previousAllocationMap.keySet(), null, knownRemotePeers, localPeerID, config, allocatorParameters);
	}

}
