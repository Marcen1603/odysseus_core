package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;

public class MinCPULoadBalancer extends AbstractSelectPeerLoadBalancer {

	@Override
	protected Optional<PeerID> getNextPeer(Map<PeerID, Usage> remainingPeers, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		Usage min = new Usage();
		min.mem = Long.MAX_VALUE;
		min.cpu = Double.MAX_VALUE;

		PeerID minPeerID = null;
		for (PeerID peerID : remainingPeers.keySet()) {
			Usage usage = remainingPeers.get(peerID);
			if( !avoidingPeers.contains(peerID) && usage.cpu < min.cpu ) {
				min = usage;
				minPeerID = peerID;
			}
		}

		return Optional.fromNullable(minPeerID);
	}

}
