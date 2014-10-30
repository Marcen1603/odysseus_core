package de.uniol.inf.is.odysseus.peer.recovery;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import de.uniol.inf.is.odysseus.peer.recovery.messages.RecoveryInstructionMessage;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand
 *
 */
public interface IRecoveryCommunicator {

	/**
	 * Does the recovery for the failed peer (and checks, if this peer can and
	 * should to this). This is the method you should call if you notice that a
	 * peer failed.
	 * 
	 * @param failedPeer
	 *            The PeerID from the failed peer
	 */
	public void recover(PeerID failedPeer);

	/**
	 * Experimental: Sends a message to all given peers, that they should hold
	 * on for the respective shared query id. (So, peers[0] should hold on for
	 * queryIds[0], peers[1] for queryIds[1] and so on)
	 * 
	 * @param peers
	 *            List of peer which should on for the respective queryId
	 * @param queryIds
	 *            List of query-ids for which the respective peer should hold on
	 */
	public void sendHoldOnMessage(PeerID peerToHoldOn, RecoveryInstructionMessage holdOnMessage);

	/**
	 * Sends a message to the receiverPeer, that this peer has to update it's
	 * JxtaReceiverPO with the given pipeId, so that this peer get the tuples
	 * from newSenderPeer
	 * 
	 * @param receiverPeer
	 *            The peer on which the receiver is installed which has to be
	 *            updated
	 * @param newSenderPeer
	 *            The peer with the new sender
	 * @param pipeId
	 *            The pipeId which is affected
	 */
	public void sendUpdateReceiverMessage(PeerID receiverPeer, PeerID newSenderPeer, PipeID pipeId);

	/**
	 * Sends a recovery-agreement message to all known peers. This message tells
	 * the other peers, that this peer wants to do the recovery for the failed
	 * peer for the given sharedQueryId
	 * 
	 * @param failedPeer
	 *            The failed peer this peer wants to do recovery for
	 * @param sharedQueryId
	 *            The shared query id, for which this peer wants to do the
	 *            recovery
	 */
	public void sendRecoveryAgreementMessage(PeerID failedPeer, ID sharedQueryId);

	/**
	 * Sends a message with backup information to another peer.
	 * 
	 * @param destination
	 *            The peer for which the backup information are. <br />
	 *            Must be not null.
	 * @param info
	 *            The backup information to send. <br />
	 *            Must be not null.
	 */
	public void sendBackupInformation(PeerID destination, IRecoveryBackupInformation info);

	/**
	 * Sends a message to the new peer. The message contains the PQL from the
	 * failed peer for the given sharedQueryId. The newPeer, which receives the
	 * message, will (try to) install the queries.
	 * 
	 * @param failedPeer
	 *            The failed peer
	 * @param newPeer
	 *            The new peer to take over a part of the work of the failed
	 *            peer
	 * @param sharedQueryId
	 *            The id of the shared query which the new peer should recover
	 *            from the failed peer
	 */
	// TODO Better way: allocate each single query part new. M.B.
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer, ID sharedQueryId);

	/**
	 * The RecoveryCommunicator will search for a buddy peer which will save
	 * that it's the buddy for this peer for this specific shared query. It will
	 * also send the necessary backup-information to this peer
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query for which the other peer will be
	 *            the buddy (and thus will do the recovery if this peer fails)
	 */
	public void chooseBuddyForQuery(ID sharedQueryId);

	/**
	 * Sends a message to the given peer that it has to go on sending the tuples
	 * for the given pipeId. (Stop buffering)
	 * 
	 * @param receiverPeer
	 *            The peer which should stop buffering and going on to send the
	 *            tuples
	 * @param pipeId
	 *            The pipeId for which the receiver of this message should go on
	 *            sending tuples
	 */
	public void sendGoOnMessage(PeerID receiverPeer, PipeID pipeId);

}