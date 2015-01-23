package de.uniol.inf.is.odysseus.p2p_new;

public interface IPeerReachabilityListener {

	public void peerReachable( PeerReachabilityInfo info );
	public void peerNotReachable( PeerReachabilityInfo info );
	
}
