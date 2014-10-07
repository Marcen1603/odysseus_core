package de.uniol.inf.is.odysseus.peer.recovery.allocation.roundrobin;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import net.jxta.peer.PeerID;

/**
 * The round robin allocator, which excludes the local peer.
 * Adaption of {@link de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin.RoundRobinAllocator}
 * @author Simon Kuespert
 */
public class RoundRobinAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		return "roundrobin";
	}

	@Override
	protected List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID) {
		
		return Lists.newArrayList(knownRemotePeers);
		
	}

}