package de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import net.jxta.peer.PeerID;

/**
 * The round robin allocator, which excludes the local peer.
 * @author Michael Brand
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