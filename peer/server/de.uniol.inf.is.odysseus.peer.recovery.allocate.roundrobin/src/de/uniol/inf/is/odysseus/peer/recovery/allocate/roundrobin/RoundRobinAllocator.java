package de.uniol.inf.is.odysseus.peer.recovery.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import net.jxta.peer.PeerID;

/**
 * The round robin allocator, which excludes the local peer.
 * 
 * Copied from Load-Balancing de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin
 * 
 * @author Simon Kuespert
 */
public class RoundRobinAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		return "roundrobin";
	}

	@Override
	protected List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID, Collection<PeerID> peersToIgnore ) {
		List<PeerID> peers = Lists.newArrayList(knownRemotePeers);
		peers.removeAll(peersToIgnore);
		return peers;
	}

}