package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.jxta.id.ID;
import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;

/**
 * This interface describes the actions that are taken, when a new peer needs to
 * be found and queries of failed peers need to be allocated.
 * 
 * @author Simon Kuespert
 *
 */
public interface IRecoveryDynamicBackup {

	/**
	 * Initiates the agreement over which peer executes the recovery.
	 * 
	 * @param failedPeer
	 *            PeerID of the failed peer.
	 * @param sharedQueryId
	 *            ID of the query to be recovered.
	 * @param newPeer
	 *            The peer where we want to install the parts of the query from
	 *            the failed peer
	 * @param queryPart 
	 * @param recoveryStateIdentifier 
	 */
	public void initiateAgreement(PeerID failedPeer, ID sharedQueryId, PeerID newPeer, ILogicalQueryPart queryPart, UUID recoveryStateIdentifier);


	/**
	 * Checks for which query this peer is responsible and gets the list with
	 * the shared query ids to be recovered by this peer.
	 * 
	 * @param failedPeer
	 *            PeerID of the failed peer
	 * @return List with the shared query ids to be recovered by this peer.
	 */
	public List<ID> getSharedQueryIdsForRecovery(PeerID failedPeer);

	/**
	 * Determines which tuples sent to the failed peer have to be hold on and
	 * sends a message to the affected peer.
	 * 
	 * @param sharedQueryId
	 *            ID of the query to be hold on.
	 * @param failedPeer
	 *            PeerID of the failed peer.
	 * @param recoveryStateIdentifier 
	 */
	public void determineAndSendHoldOnMessages(ID sharedQueryId, PeerID failedPeer, UUID recoveryStateIdentifier);

	/**
	 * Checks, if that peer was responsible for me (if he was a buddy for me)
	 * and deletes him from the list (e.g. cause he failed)
	 * If so, this method initiates, that we get a new buddy
	 * 
	 * @param failedPeer
	 *            The peer which failed
	 * @return The list with the shared query-ids, if it was a buddy for me,
	 *         null, if not
	 */
	public List<ID> removeMyBuddyAndSearchNew(PeerID failedPeer);
	
	/**
	 * Allocates QueryParts of a failed peer to a new peer.	 
	 * @param sharedQueryId Query that needs allocation
	 * @param recoveryAllocator The specific recovery allocator given by the strategy
	 * @param failedPeer PeerID of the failed peer
	 * @return Map<ILogicalQueryPart, PeerID> of the new peers to allocate to
	 */

	public Map<ILogicalQueryPart, PeerID> allocateToNewPeer(PeerID failedPeer, ID sharedQueryId, IRecoveryAllocator recoveryAllocator) throws QueryPartAllocationException;


	public Map<ILogicalQueryPart, PeerID> reallocateToNewPeer(
			Map<ILogicalQueryPart, PeerID> allocationMap,
			List<PeerID> inadequatePeers,
			IRecoveryAllocator recoveryAllocator) throws QueryPartAllocationException;


	RecoveryProcessState getRecoveryProcessState(UUID recoveryProcessStateId);
}
