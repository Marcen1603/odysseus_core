package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class AbstractSelectPeerLoadBalancer extends AbstractLoadBalancer {

	@Override
	protected final List<PeerID> sortPeerIDs(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		
		List<PeerID> sortedPeers = Lists.newArrayList();
		Map<PeerID, Usage> usagesCopy = Maps.newHashMap(currentUsages);	
		
		Optional<PeerID> optMinimumPeer = getNextPeer(usagesCopy, avoidingPeers, newMemCost, newCpuCost);
		while( optMinimumPeer.isPresent() ) {
			PeerID minimumPeer = optMinimumPeer.get();
			sortedPeers.add(minimumPeer);
			usagesCopy.remove(minimumPeer);
			
			optMinimumPeer = usagesCopy.isEmpty() ? Optional.<PeerID>absent() : getNextPeer(usagesCopy, avoidingPeers, newMemCost, newCpuCost);
		}
		
		if( !usagesCopy.isEmpty() ) {
			// avoidedPeers was not empty!
			
			optMinimumPeer = getNextPeer(usagesCopy, Lists.<PeerID>newArrayList(), newMemCost, newCpuCost);
			while( optMinimumPeer.isPresent() ) {
				PeerID minimumPeer = optMinimumPeer.get();
				sortedPeers.add(minimumPeer);
				usagesCopy.remove(minimumPeer);
				
				optMinimumPeer = usagesCopy.isEmpty() ? Optional.<PeerID>absent() : getNextPeer(usagesCopy, Lists.<PeerID>newArrayList(), newMemCost, newCpuCost);
			}
		}
		
		return sortedPeers;
	}
	
	protected abstract Optional<PeerID> getNextPeer( Map<PeerID, Usage> remainingPeers, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost );

}
