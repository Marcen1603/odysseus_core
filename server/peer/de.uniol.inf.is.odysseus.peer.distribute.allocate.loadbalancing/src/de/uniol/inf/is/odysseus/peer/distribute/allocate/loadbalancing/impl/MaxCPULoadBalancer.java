package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import net.jxta.peer.PeerID;

public class MaxCPULoadBalancer extends AbstractLoadBalancer {

	@Override
	protected PeerID selectPeerID(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		Optional<PeerID> maxPeerID = getMaximumPeer(currentUsages, avoidingPeers, newMemCost, newCpuCost);
		if( !maxPeerID.isPresent() ) {
			// no avoid now
			maxPeerID = getMaximumPeer(currentUsages, Lists.<PeerID>newArrayList(), newMemCost, newCpuCost);
		}

		return maxPeerID.get();
	}

	private static Optional<PeerID> getMaximumPeer(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
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
