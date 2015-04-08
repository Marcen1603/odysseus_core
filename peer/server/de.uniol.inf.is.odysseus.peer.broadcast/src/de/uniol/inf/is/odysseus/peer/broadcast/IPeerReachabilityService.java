package de.uniol.inf.is.odysseus.peer.broadcast;

import java.util.Collection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.peer.broadcast.impl.IPeerReachabilityListener;
import net.jxta.peer.PeerID;

public interface IPeerReachabilityService {

	public boolean isPeerReachable( PeerID peerID );
	
	public Collection<PeerID> getReachablePeers();
	public Optional<PeerReachabilityInfo> getReachabilityInfo( PeerID peerID );
	
	public void addListener( IPeerReachabilityListener listener );
	public void removeListener( IPeerReachabilityListener listener );
}
