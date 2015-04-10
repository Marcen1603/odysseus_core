package de.uniol.inf.is.odysseus.peer.communication;

import net.jxta.peer.PeerID;

public interface IPeerCommunicatorListener {

	public void receivedMessage( IPeerCommunicator communicator, PeerID senderPeer, IMessage message );
	
}
