package de.uniol.inf.is.odysseus.peer.recovery;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.AddQueryResponseMessage;

/**
 * A Handler for working with AddQueryResponse Messages. This messages could be
 * an ack or fail message. Both types need to be handled here.
 * 
 * @author ChrisToenjesDeye
 * 
 */
public interface IAddQueryResponseHandler {

	/**
	 * method for handling the response. Both types (ack and fail) should be
	 * handled.
	 * 
	 * @param senderPeer
	 *            - sender of the message
	 * @param responseMessage
	 *            - ADDQueryResponse Message
	 * @param recoveryCommunicator
	 *            - communicator needed for some actions
	 */
	void handleAddQueryResponse(PeerID senderPeer,
			AddQueryResponseMessage responseMessage,
			IRecoveryCommunicator recoveryCommunicator);
}
