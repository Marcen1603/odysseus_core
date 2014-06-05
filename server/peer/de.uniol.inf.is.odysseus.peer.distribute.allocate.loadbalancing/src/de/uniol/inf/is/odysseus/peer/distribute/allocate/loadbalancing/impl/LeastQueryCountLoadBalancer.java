package de.uniol.inf.is.odysseus.peer.distribute.allocate.loadbalancing.impl;

import java.util.Collection;
import java.util.Map;

import net.jxta.peer.PeerID;

import com.google.common.base.Optional;


public class LeastQueryCountLoadBalancer extends AbstractSelectPeerLoadBalancer {

	@Override
	protected Optional<PeerID> getNextPeer(Map<PeerID, Usage> remainingPeers, Collection<PeerID> avoidingPeers, long newMemCost, double newCpuCost) {
		
		PeerID resultPeerID = null;
		int minQueryCount = Integer.MAX_VALUE;
		
		for( PeerID pid : remainingPeers.keySet() ) {
			Usage usage = remainingPeers.get(pid);
			if( usage.runningQueriesCount < minQueryCount ) {
				resultPeerID = pid;
				minQueryCount = usage.runningQueriesCount;
			}
		}
		
		return Optional.fromNullable(resultPeerID);
	}

}
