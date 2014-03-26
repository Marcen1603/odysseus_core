package de.uniol.inf.is.odysseus.p2p_new;

import com.google.common.collect.ImmutableCollection;

import net.jxta.peer.PeerID;

public interface IPeerCommunicator {

	public ImmutableCollection<PeerID> getConnectedPeers();
	public boolean isConnected( PeerID destinationPeer );
	public void send( PeerID destinationPeer, int messageID, byte[] message ) throws PeerCommunicationException;
	
	public void addListener( int id, IPeerCommunicatorListener listener );
	public void removeListener( int id, IPeerCommunicatorListener listener );
}
