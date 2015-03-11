package de.uniol.inf.is.odysseus.peer.distribute.allocate.querycount;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.peer.resource.IPeerResourceUsageManager;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;

public class PeerQueryCountMap {

	private static final Logger LOG = LoggerFactory.getLogger(PeerQueryCountMap.class);
	
	private final Map<PeerID, Integer> peerQueryCountMap = Maps.newHashMap();
	
	public PeerQueryCountMap( Collection<PeerID> peerIDs, IPeerResourceUsageManager manager ) {
		Preconditions.checkNotNull(peerIDs, "Collection of peerIDs must not be null!");
		Preconditions.checkArgument(!peerIDs.isEmpty(), "Collection of peerIDs must not be empty!");
		Preconditions.checkNotNull(manager, "PeerResourceUsageManager must not be null!");
		
		for( PeerID peerID : peerIDs ) {
			try {
				Future<Optional<IResourceUsage>> remoteUsageFuture = manager.getRemoteResourceUsage(peerID);
				Optional<IResourceUsage> optRemoteUsage = remoteUsageFuture.get();
				if( optRemoteUsage.isPresent() ) {
					peerQueryCountMap.put(peerID, optRemoteUsage.get().getRunningQueriesCount());
				} else {
					peerQueryCountMap.put(peerID, 0);
				}
			} catch (InterruptedException | ExecutionException e) {
				LOG.warn("Could not determine resource usage", e);
				
				peerQueryCountMap.put(peerID, 0);
			}
		}
	}
	
	public PeerID getPeerIDWithLowestQueryCount() {
		PeerID minPeerID = null;
		int minQueryCount = Integer.MAX_VALUE;
		
		for( PeerID peerID : peerQueryCountMap.keySet() ) {
			Integer queryCount = peerQueryCountMap.get(peerID);
			if( queryCount < minQueryCount ) {
				minQueryCount = queryCount;
				minPeerID = peerID;
			}
		}
		
		return minPeerID;
	}

	public void incrementQueryCount(PeerID peerID) {
		if( peerQueryCountMap.containsKey(peerID) ) {
			peerQueryCountMap.put( peerID, peerQueryCountMap.get(peerID) + 1);
		}
	}
}
