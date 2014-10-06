package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;

/**
 * A recovery communicator handles the communication between peers for recovery
 * mechanisms.
 * 
 * @author Tobias Brandt & Michael Brand
 *
 */
public interface IRecoveryCommunicator {

	/**
	 * Sends the backup information for a given peer to that peer.
	 * 
	 * @param peerId
	 *            The ID identifying the peer.
	 * @param sharedQueryId
	 *            The ID identifying the distributed query.
	 * @param backupInformation
	 *            The backup information as a collection of query parts, which
	 *            the peer shall store.
	 */
	public void sendBackupInformation(PeerID peerId, ID sharedQueryId,
			Map<PeerID, Collection<String>> backupInformation);

	// TODO javaDoc M.B.
	public void sendTakeOver(ID sharedQueryId, String pqlStatement, PeerID peerId);

	// TODO should be changed. See comments within implementation M.B.
	public void installQueriesOnNewPeer(PeerID failedPeer, PeerID newPeer, ID sharedQueryId);

	public void sendBackupJxtaInformation(PeerID peerId, ID sharedQueryId,
			String key, String value);

	public void recover(PeerID failedPeer);

	public void sendHoldOnMessages(List<PeerID> peers, List<ID> queryIds);

	public void sendNewReceiverMessage(PeerID senderPeer,
			PeerID newReceiverPeer, ID sharedQueryID);

	public void sendRecoveryAgreementMessage(PeerID failedPeer, ID sharedQueryId);

}