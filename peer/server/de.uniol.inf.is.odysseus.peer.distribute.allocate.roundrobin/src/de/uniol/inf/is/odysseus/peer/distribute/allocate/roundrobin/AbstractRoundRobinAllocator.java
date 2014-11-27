package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.IQueryPartAllocator;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;

public abstract class AbstractRoundRobinAllocator implements IQueryPartAllocator {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractRoundRobinAllocator.class);
	
	public static final String MONITOR_APP_NAME = "PeerMonitorServer";
	
	@Override
	public Map<ILogicalQueryPart, PeerID> allocate(Collection<ILogicalQueryPart> queryParts, ILogicalQuery query, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		LOG.debug("Begin round robin allocation");
		
		List<PeerID> peerIDs = determineConsideredPeerIDs(knownRemotePeers, localPeerID, determinePeersToIgnore(knownRemotePeers));
		if( LOG.isDebugEnabled() ) {
			logPeers(peerIDs);
		}
		
		if( peerIDs == null || peerIDs.isEmpty() ) {
			throw new QueryPartAllocationException("There are no peers to be considered for round robin");
		}
		
		return roundRobinImpl(queryParts, peerIDs);
	}

	private Set<PeerID> determinePeersToIgnore(
			Collection<PeerID> knownRemotePeers) throws QueryPartAllocationException {
		if(RoundRobinActivator.getPeerDictionary() == null) {			
			throw new QueryPartAllocationException("No peer dictionary set");			
		}
		
		Set<PeerID> peersToIgnore = Sets.newHashSet();
		for(PeerID peer : knownRemotePeers) {
			if(RoundRobinActivator.getPeerDictionary().getRemotePeerName(peer).equals(MONITOR_APP_NAME)) {
				peersToIgnore.add(peer);
			}
		}
		return peersToIgnore;
	}

	@Override
	public Map<ILogicalQueryPart, PeerID> reallocate(Map<ILogicalQueryPart, PeerID> previousAllocationMap, Collection<PeerID> faultyPeers, Collection<PeerID> knownRemotePeers, PeerID localPeerID, QueryBuildConfiguration config, List<String> allocatorParameters) throws QueryPartAllocationException {
		LOG.debug("Begin round robin reallocation");
		
		List<PeerID> peerIDs = determineConsideredPeerIDs(knownRemotePeers, localPeerID, determinePeersToIgnore(knownRemotePeers));
		peerIDs.removeAll(faultyPeers);
		if( LOG.isDebugEnabled() ) {
			logPeers(peerIDs);
		}
		
		if( peerIDs == null || peerIDs.isEmpty() ) {
			throw new QueryPartAllocationException("There are no peers left to be considered for round robin in reallocation");
		}
		
		return roundRobinImpl(previousAllocationMap.keySet(), peerIDs);
	}
	
	private static Map<ILogicalQueryPart, PeerID> roundRobinImpl(Collection<ILogicalQueryPart> queryParts, List<PeerID> peerIDs) {
		int peerIDIndex = 0;
		
		Map<ILogicalQueryPart, PeerID> allocationMap = Maps.newHashMap();
		
		for( ILogicalQueryPart queryPart : queryParts ) {
			if( LOG.isDebugEnabled() ) {
				LOG.debug("Allocating query part {}", queryPart);
				if( !queryPart.getAvoidingQueryParts().isEmpty() ) {
					LOG.debug("Avoiding query parts");
					for( ILogicalQueryPart avoidedQueryPart : queryPart.getAvoidingQueryParts() ) {
						LOG.debug("\t{}", avoidedQueryPart);
					}
				}
			}
			
			Collection<PeerID> nonAllowedPeers = determineAvoidedPeers(queryPart, allocationMap);
			if( !nonAllowedPeers.isEmpty() ) {
				LOG.debug("Avoiding peers for this query part:");
				logPeers(nonAllowedPeers);
			}
			
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
				LOG.debug("Could not allocate to a peer which is not avoided...");
				peerIDIndex = ( peerIDIndex + 1 ) % peerIDs.size();
				chosenPeerID = peerIDs.get(peerIDIndex);
			} else {
				LOG.debug("Could allocate to non-avoided peer");
				peerIDIndex = tempPeerIDIndex;
			}
			
			LOG.debug("Allocated to peer {} for query part {}", RoundRobinActivator.getPeerDictionary().getRemotePeerName(chosenPeerID), queryPart);
			allocationMap.put(queryPart, chosenPeerID);
		}
		
		LOG.debug("RoundRobinAllocation finished");
		return allocationMap;
	}

	private static void logPeers(Collection<PeerID> peerIDs) {
		if( peerIDs == null || peerIDs.isEmpty() ) {
			LOG.debug("\t<empty>");
			return;
		}
		
		for( PeerID peerID : peerIDs ) {
			LOG.debug("\t{}", RoundRobinActivator.getPeerDictionary().getRemotePeerName(peerID));
		}
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

	protected abstract List<PeerID> determineConsideredPeerIDs( Collection<PeerID> knownRemotePeers, PeerID localPeerID, Collection<PeerID> peersToIgnore );
}
