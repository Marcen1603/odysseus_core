package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import net.jxta.pipe.PipeID;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionaryListener;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand & Simon Kuespert
 * 
 */
public interface IRecoveryCommunicator extends IPeerDictionaryListener {

	/**
	 * Experimental: Sends a message to all given peers, that they should hold
	 * on for the respective shared query id. (So, peers[0] should hold on for
	 * queryIds[0], peers[1] for queryIds[1] and so on)
	 * 
	 * @param recoveryStateIdentifier
	 * 
	 * @param peers
	 *            List of peer which should on for the respective queryId
	 * @param queryIds
	 *            List of query-ids for which the respective peer should hold on
	 */
	// TODO handle result. M.B.
	public boolean sendHoldOnMessage(PeerID receiverPeer, PipeID pipeId);

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
	// TODO handle result. M.B.
	public boolean sendUpdateReceiverMessage(PeerID receiverPeer,
			PeerID newSenderPeer, PipeID pipeId, ID sharedQueryId);

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
	// TODO handle result. M.B.
	public boolean sendRecoveryAgreementMessage(PeerID failedPeer,
			ID sharedQueryId);

	/**
	 * Sends a message with backup information to another peer.
	 * 
	 * @param destination
	 *            The peer for which the backup information are. <br />
	 *            Must be not null.
	 * @param info
	 *            The backup information to send. <br />
	 *            Must be not null.
	 * @param isNewInfo
	 *            True, is this is a new backup-information or false, if this is
	 *            an updated information
	 * @return true, if an acknowledge received; false, else.
	 */
	// TODO handle result. M.B.
	public boolean sendBackupInformation(PeerID destination,
			IRecoveryBackupInformation info, boolean isNewInfo);

	/**
	 * Distributes the backup-information as updated information to every peer,
	 * so that the peer who are affected can update their information
	 * 
	 * @param info
	 *            the updated backup-information
	 */
	public void distributeUpdatedBackupInfo(IRecoveryBackupInformation info);

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
	 * @param pql
	 *            The PQL string with the query arguements to be installed
	 * @param recoveryStateIdentifier
	 */
	// TODO Better way: allocate each single query part new. M.B.
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer,
			ID sharedQueryId, String pql, UUID recoveryStateIdentifier,
			UUID subprocessID);

	/**
	 * The RecoveryCommunicator will search for a buddy peer which will save
	 * that it's the buddy for this peer for this specific shared query. It will
	 * also send the necessary backup-information to this peer
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query for which the other peer will be
	 *            the buddy (and thus will do the recovery if this peer fails)
	 */
	// TODO handle result. M.B.
	public boolean chooseBuddyForQuery(ID sharedQueryId);

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
	// TODO handle result. M.B.
	public boolean sendGoOnMessage(PeerID receiverPeer, PipeID pipeId);

	/**
	 * Returns the RecoveryProcessState for a given identifier
	 * 
	 * @param identifier
	 * @return RecoveryProcessState
	 */
	RecoveryProcessState getRecoveryProcessState(UUID identifier);

	/**
	 * Removes an active recoveryProcessState for a given identifier
	 * 
	 * @param identifier
	 *            of the processState
	 */
	void removeRecoveryProcessState(UUID identifier);

	/**
	 * TODO
	 * 
	 * @param strategyManager
	 */
	void setRecoveryStrategyManager(IRecoveryStrategyManager strategyManager);

	/**
	 * Removes a shared query id from the list of the ids, for which this peer
	 * needs a buddy but was not able to find one
	 * 
	 * @param sharedQueryId
	 *            The id of the shared query to remove
	 * @return true, if there was a id to remove, false, if not
	 */
	public boolean removeSharedQueryIdFromNoBuddyList(ID sharedQueryId);

}