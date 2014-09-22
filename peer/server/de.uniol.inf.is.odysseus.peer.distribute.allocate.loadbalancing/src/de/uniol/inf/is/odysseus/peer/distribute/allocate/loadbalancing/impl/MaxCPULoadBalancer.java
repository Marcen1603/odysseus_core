package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;

public class MaxCPULoadBalancer extends AbstractSelectPeerLoadBalancer {

	@Override
	protected Optional<PeerID> getNextPeer(Map<PeerID, Usage> remainingPeers, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		Usage max = new Usage();
		max.mem = Long.MIN_VALUE;
		max.cpu = Double.MIN_VALUE;

		PeerID maxPeerID = null;
		for (PeerID peerID : remainingPeers.keySet()) {
			Usage usage = remainingPeers.get(peerID);
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
