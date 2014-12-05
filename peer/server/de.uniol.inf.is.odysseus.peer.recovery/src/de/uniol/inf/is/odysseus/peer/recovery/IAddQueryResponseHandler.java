package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryAddQueryResponseMessage;

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
	 * @param communicator
	 *            - active recovery communicator
	 * @param responseMessage
	 *            - ADDQueryResponse Message
	 * @param processId
	 *            The id of the recovery process. <br />
	 *            Must be not null.
	 * @param subprocessId
	 *            The id of the recovery subprocess. <br />
	 *            Must be not null.
	 * @param pql
	 *            The PQL code to execute. <br />
	 *            Must be not null.
	 * @param sharedQueryId
	 *            The affected shared query. <br />
	 *            Must be not null.
	 */
	void handleAddQueryResponse(PeerID senderPeer,
			IRecoveryCommunicator communicator,
			RecoveryAddQueryResponseMessage responseMessage,
			UUID processStateId, UUID subprocessId, String pql, ID sharedQueryId);
}
