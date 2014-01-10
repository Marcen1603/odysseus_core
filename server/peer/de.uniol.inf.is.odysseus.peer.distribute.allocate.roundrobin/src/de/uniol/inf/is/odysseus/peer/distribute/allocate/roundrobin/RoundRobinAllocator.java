package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import net.jxta.peer.PeerID;

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
