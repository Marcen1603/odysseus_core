package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MaxCPULoadBalancer extends AbstractLoadBalancer {

	@Override
	protected List<PeerID> sortPeerIDs(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		
		List<PeerID> sortedPeers = Lists.newArrayList();
		Map<PeerID, Usage> usagesCopy = Maps.newHashMap(currentUsages);	
		
		Optional<PeerID> optMinimumPeer = getMaximumPeer(currentUsages, avoidingPeers, newMemCost, newCpuCost);
		while( optMinimumPeer.isPresent() ) {
			PeerID minimumPeer = optMinimumPeer.get();
			sortedPeers.add(minimumPeer);
			usagesCopy.remove(minimumPeer);
			
			optMinimumPeer = getMaximumPeer(currentUsages, avoidingPeers, newMemCost, newCpuCost);
		}
		
		if( !usagesCopy.isEmpty() ) {
			// avoidedPeers was not empty!
			
			optMinimumPeer = getMaximumPeer(currentUsages, avoidingPeers, newMemCost, newCpuCost);
			while( optMinimumPeer.isPresent() ) {
				PeerID minimumPeer = optMinimumPeer.get();
				sortedPeers.add(minimumPeer);
				usagesCopy.remove(minimumPeer);
				
				optMinimumPeer = getMaximumPeer(currentUsages, avoidingPeers, newMemCost, newCpuCost);
			}
		}
		
		return sortedPeers;
	}

	private static Optional<PeerID> getMaximumPeer(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		if( currentUsages.isEmpty() ) {
			return Optional.absent();
		}
		
		Usage max = new Usage();
		max.mem = Long.MIN_VALUE;
		max.cpu = Double.MIN_VALUE;

		PeerID maxPeerID = null;
		for (PeerID peerID : currentUsages.keySet()) {
			Usage usage = currentUsages.get(peerID);
			if( !avoidingPeers.contains(peerID) && usage.cpu > max.cpu && doNotOverload(usage, newMemCost, newCpuCost)) {
				max = usage;
				maxPeerID = peerID;
			}
		}
		return Optional.fromNullable(maxPeerID);
	}

	private static boolean doNotOverload(Usage usage, long newMemCost, double newCpuCost) {
		return usage.cpu + newCpuCost < usage.maxCpu && usage.mem + newMemCost < usage.maxMem;
	}

}
