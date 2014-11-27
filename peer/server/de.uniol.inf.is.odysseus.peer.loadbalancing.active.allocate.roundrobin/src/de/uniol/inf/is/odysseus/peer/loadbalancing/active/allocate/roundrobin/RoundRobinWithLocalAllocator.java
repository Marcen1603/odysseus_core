package de.uniol.inf.is.odysseus.peer.loadbalancing.active.allocate.roundrobin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.jxta.peer.PeerID;

/**
 * The round robin allocator, which includes the local peer.
 * @author Michael Brand
 */
public class RoundRobinWithLocalAllocator extends AbstractRoundRobinAllocator {

	@Override
	public String getName() {
		
		return "roundrobinwithlocal";
		
	}

	@Override
	protected List<PeerID> determineConsideredPeerIDs(Collection<PeerID> knownRemotePeers, PeerID localPeerID, Collection<PeerID> peersToIgnore ) {
		List<PeerID> peers = new ArrayList<PeerID>();
		// don't change the order of peers !!!
		// Round Robin now first allocates all other peers and after that the own peer
		peers.add(localPeerID);		
		peers.addAll(knownRemotePeers);
		// ignore monitoring app
		peers.removeAll(peersToIgnore);
		return peers;	
	}

}