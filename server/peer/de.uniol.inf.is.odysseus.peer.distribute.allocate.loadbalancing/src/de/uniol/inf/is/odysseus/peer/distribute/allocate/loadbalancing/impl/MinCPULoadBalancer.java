package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MinCPULoadBalancer extends AbstractLoadBalancer {

	@Override
	protected List<PeerID> sortPeerIDs(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		
		List<PeerID> sortedPeers = Lists.newArrayList();
		Map<PeerID, Usage> usagesCopy = Maps.newHashMap(currentUsages);	
		
		Optional<PeerID> optMinimumPeer = getMinimumPeer(usagesCopy, avoidingPeers);
		while( optMinimumPeer.isPresent() ) {
			PeerID minimumPeer = optMinimumPeer.get();
			sortedPeers.add(minimumPeer);
			usagesCopy.remove(minimumPeer);
			
			optMinimumPeer = getMinimumPeer(usagesCopy, avoidingPeers);
		}
		
		if( !usagesCopy.isEmpty() ) {
			// avoidedPeers was not empty!
			
			optMinimumPeer = getMinimumPeer(usagesCopy, Lists.<PeerID>newArrayList());
			while( optMinimumPeer.isPresent() ) {
				PeerID minimumPeer = optMinimumPeer.get();
				sortedPeers.add(minimumPeer);
				usagesCopy.remove(minimumPeer);
				
				optMinimumPeer = getMinimumPeer(usagesCopy, avoidingPeers);
			}
		}
		
		return sortedPeers;
	}

	private static Optional<PeerID> getMinimumPeer(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers) {
		if( currentUsages.isEmpty() ) {
			return Optional.absent();
		}
		
		Usage min = new Usage();
		min.mem = Long.MAX_VALUE;
		min.cpu = Double.MAX_VALUE;

		PeerID minPeerID = null;
		for (PeerID peerID : currentUsages.keySet()) {
			Usage usage = currentUsages.get(peerID);
			if( !avoidingPeers.contains(peerID) && usage.cpu < min.cpu ) {
				min = usage;
				minPeerID = peerID;
			}
		}

		return Optional.fromNullable(minPeerID);
	}

}
