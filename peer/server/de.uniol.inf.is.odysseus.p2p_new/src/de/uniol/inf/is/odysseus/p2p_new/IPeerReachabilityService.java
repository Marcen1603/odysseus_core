package de.uniol.inf.is.odysseus.p2p_new;

import java.util.Collection;

import com.google.common.base.Optional;

import net.jxta.peer.PeerID;

public interface IPeerReachabilityService {

	public boolean isPeerReachable( PeerID peerID );
	
	public Collection<PeerID> getReachablePeers();
	public Optional<PeerReachabilityInfo> getReachabilityInfo( PeerID peerID );
	
}
