package de.uniol.inf.is.odysseus.net.communication;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeCommunicator {

	public void registerMessageType( Class<? extends IMessage> messageType );
	public void unregisterMessageType( Class<? extends IMessage> messageType );
	public void send( IOdysseusNode destinationNode, IMessage message ) throws OdysseusNodeCommunicationException;
	
	public void addListener( IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType );
	public void removeListener( IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType );

}
