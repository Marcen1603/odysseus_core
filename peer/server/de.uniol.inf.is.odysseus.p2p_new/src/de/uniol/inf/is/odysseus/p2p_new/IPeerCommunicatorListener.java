package de.uniol.inf.is.odysseus.p2p_new;

import net.jxta.peer.PeerID;

public interface IPeerCommunicatorListener {

	public void receivedMessage( IPeerCommunicator communicator, PeerID senderPeer, IMessage message );
	
}
