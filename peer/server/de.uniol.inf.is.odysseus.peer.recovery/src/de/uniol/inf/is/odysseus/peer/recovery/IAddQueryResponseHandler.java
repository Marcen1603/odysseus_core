package de.uniol.inf.is.odysseus.peer.recovery;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.AddQueryResponseMessage;

public interface IAddQueryResponseHandler {
	void handleAddQueryResponse(PeerID senderPeer,
			AddQueryResponseMessage responseMessage,
			IRecoveryCommunicator recoveryCommunicator);
}
