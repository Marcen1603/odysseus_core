package de.uniol.inf.is.odysseus.net.communication;

import java.util.Collection;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeCommunicator {

	public void registerMessageType( Class<? extends IMessage> messageType );
	public void unregisterMessageType( Class<? extends IMessage> messageType );
	
	public void send( IOdysseusNode destinationNode, IMessage message ) throws OdysseusNodeCommunicationException;
	public Collection<IOdysseusNode> getDestinationNodes();
	public boolean canSend(IOdysseusNode node);
	
	public void addListener( IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType );
	public void removeListener( IOdysseusNodeCommunicatorListener listener, Class<? extends IMessage> messageType );

}
