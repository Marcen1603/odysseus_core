package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Map;

import net.jxta.peer.PeerID;

public class MaxCPULoadBalancer extends AbstractLoadBalancer {

	@Override
	protected PeerID selectPeerID(Map<PeerID, Usage> currentUsages, long newMemCost, double newCpuCost) {
		Usage max = new Usage();
		max.mem = Long.MIN_VALUE;
		max.cpu = Double.MIN_VALUE;

		PeerID minPeerID = null;
		for (PeerID peerID : currentUsages.keySet()) {
			Usage usage = currentUsages.get(peerID);
			if( usage.cpu > max.cpu && doNotOverload(usage, newMemCost, newCpuCost)) {
				max = usage;
				minPeerID = peerID;
			}
		}

		return minPeerID;
	}

	private static boolean doNotOverload(Usage usage, long newMemCost, double newCpuCost) {
		return usage.cpu + newCpuCost < usage.maxCpu && usage.mem + newMemCost < usage.maxMem;
	}

}
