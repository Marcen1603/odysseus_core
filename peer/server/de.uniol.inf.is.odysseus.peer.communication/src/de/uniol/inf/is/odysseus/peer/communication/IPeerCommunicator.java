package de.uniol.inf.is.odysseus.peer.communication;

import net.jxta.peer.PeerID;


public interface IPeerCommunicator {

	public void registerMessageType( Class<? extends IMessage> messageType );
	public void unregisterMessageType( Class<? extends IMessage> messageType );
	public void send( PeerID destinationPeer, IMessage message ) throws PeerCommunicationException;
	
	public void addListener( IPeerCommunicatorListener listener, Class<? extends IMessage> messageType );
	public void removeListener( IPeerCommunicatorListener listener, Class<? extends IMessage> messageType );
}
