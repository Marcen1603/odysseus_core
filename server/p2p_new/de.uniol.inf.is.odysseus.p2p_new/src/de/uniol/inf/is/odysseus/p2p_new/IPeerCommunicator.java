package de.uniol.inf.is.odysseus.p2p_new;

import com.google.common.collect.ImmutableCollection;

import net.jxta.peer.PeerID;

public interface IPeerCommunicator {

	public ImmutableCollection<PeerID> getConnectedPeers();
	public boolean isConnected( PeerID destinationPeer );
	public void send( PeerID destinationPeer, byte[] message ) throws PeerCommunicationException;
	
	public void addListener( IPeerCommunicatorListener listener );
	public void removeListener( IPeerCommunicatorListener listener );
}
