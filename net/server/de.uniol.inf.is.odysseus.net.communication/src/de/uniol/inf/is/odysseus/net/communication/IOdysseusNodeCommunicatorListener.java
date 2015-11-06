package de.uniol.inf.is.odysseus.net.communication;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;

public interface IOdysseusNodeCommunicatorListener {

	public void receivedMessage( IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message );
	
}
