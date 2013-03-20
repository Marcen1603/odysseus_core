package de.uniol.inf.is.odysseus.p2p_new;

public interface IPeerListener {

	public void onPeerFound(IPeerManager sender, String peerID);

	public void onPeerLost(IPeerManager sender, String peerID);

}
