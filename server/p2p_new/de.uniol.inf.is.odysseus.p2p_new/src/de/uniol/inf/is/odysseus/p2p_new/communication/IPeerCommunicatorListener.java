package de.uniol.inf.is.odysseus.p2p_new.communication;

import net.jxta.peer.PeerID;

public interface IPeerCommunicatorListener {

	public void receivedMessage( IPeerCommunicator communciator, PeerID senderPeer, byte[] message );
	
}
