package de.uniol.inf.is.odysseus.peer.recovery.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;

/**
 * The round robin allocator, which includes the local peer.
 * 
 * Copied from Load-Balancing de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin
 * 
 * @author Simon Kuespert
 */
public class RoundRobinWithLocalAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		
		return "roundrobinwithlocal";
		
	}

	@Override
	protected List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID, Collection<PeerID> peersToIgnore ) {
		List<PeerID> peers = Lists.newArrayList(knownRemotePeers);
		peers.add(localPeerID);
		peers.removeAll(peersToIgnore);
		return peers;
	}

}