package de.uniol.inf.is.odysseus.peer.distribute.allocate.roundrobin;

import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

import com.google.common.collect.Lists;

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
