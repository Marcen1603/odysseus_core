package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import net.jxta.peer.PeerID;

public class MinCPULoadBalancer extends AbstractLoadBalancer {

	@Override
	protected PeerID selectPeerID(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		Optional<PeerID> minimumPeer = getMinimumPeer(currentUsages, avoidingPeers);
		if( !minimumPeer.isPresent() ) {
			// no avoiding now
			minimumPeer = getMinimumPeer(currentUsages, Lists.<PeerID>newArrayList());
		}
		
		return minimumPeer.get();
	}

	private static Optional<PeerID> getMinimumPeer(Map<PeerID, Usage> currentUsages, Collection<PeerID> avoidingPeers) {
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
