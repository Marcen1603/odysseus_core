package de.uniol.inf.is.odysseus.peer.recovery.allocation.roundrobin;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;

/**
 * The round robin allocator, which includes the local peer.
 * Adaption of {@link de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin.RoundRobinWithLocalAllocator}
 * @author Simon Kuespert
 */
public class RoundRobinWithLocalAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		
		return "roundrobinwithlocal";
		
	}

	@Override
	protected List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID) {
		
		List<PeerID> peers = Lists.newArrayList(knownRemotePeers);		
		peers.add(localPeerID);		
		return peers;
		
	}

}