package de.uniol.inf.is.odysseus.p2p_new.communication;

import net.jxta.peer.PeerID;

public interface IPeerCommunicator {

	public boolean isConnected( PeerID destinationPeer );
	public void send( PeerID destinationPeer, byte[] message ) throws PeerCommunicationException;
	
}
