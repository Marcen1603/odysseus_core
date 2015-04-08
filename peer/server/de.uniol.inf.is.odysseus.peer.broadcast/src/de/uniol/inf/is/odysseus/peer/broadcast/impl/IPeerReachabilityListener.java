package de.uniol.inf.is.odysseus.peer.broadcast.impl;

import de.uniol.inf.is.odysseus.peer.broadcast.PeerReachabilityInfo;

public interface IPeerReachabilityListener {

	public void peerReachable( PeerReachabilityInfo info );
	public void peerNotReachable( PeerReachabilityInfo info );
	
}
