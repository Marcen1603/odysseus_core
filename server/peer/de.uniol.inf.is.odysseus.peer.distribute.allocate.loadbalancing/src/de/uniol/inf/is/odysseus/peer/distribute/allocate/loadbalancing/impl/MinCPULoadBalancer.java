package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Map;

import net.jxta.peer.PeerID;

public class MinCPULoadBalancer extends AbstractLoadBalancer {

	@Override
	protected PeerID selectPeerID(Map<PeerID, Usage> currentUsages, long newMemCost, double newCpuCost) {
		Usage min = new Usage();
		min.mem = Long.MAX_VALUE;
		min.cpu = Double.MAX_VALUE;

		PeerID minPeerID = null;
		for (PeerID peerID : currentUsages.keySet()) {
			Usage usage = currentUsages.get(peerID);
			if( usage.cpu < min.cpu ) {
				min = usage;
				minPeerID = peerID;
			}
		}

		return minPeerID;
	}

}
