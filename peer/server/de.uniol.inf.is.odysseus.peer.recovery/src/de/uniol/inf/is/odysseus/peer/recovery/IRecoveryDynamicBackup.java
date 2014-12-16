package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.peer.distribute.ILogicalQueryPart;
import de.uniol.inf.is.odysseus.peer.distribute.QueryPartAllocationException;
import de.uniol.inf.is.odysseus.peer.recovery.internal.RecoveryProcessState;

/**
 * This interface describes the actions that are taken, when a new peer needs to be found and queries of failed peers
 * need to be allocated.
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
	 *            The peer where we want to install the parts of the query from the failed peer
	 * @param queryPart
	 * @param recoveryStateIdentifier
	 */
	public void initiateAgreement(PeerID failedPeer, int localQueryId, QueryState queryState, PeerID newPeer, ILogicalQueryPart queryPart,
			UUID recoveryStateIdentifier, UUID subprocessID);

	/**
	 * Determines which tuples sent to the failed peer have to be hold on and sends a message to the affected peer.
	 * 
	 * @param sharedQueryId
	 *            ID of the query to be hold on.
	 * @param failedPeer
	 *            PeerID of the failed peer.
	 * @param recoveryStateIdentifier
	 */
	public void determineAndSendHoldOnMessages(int localQueryId, PeerID failedPeer, UUID recoveryStateIdentifier);

	/**
	 * Reallocates QueryParts of a failed peer to a new peer.
	 * 
	 * @param allocationMap
	 *            - the previous allocation map
	 * @param inadequatePeers
	 *            - peers which cannot install the querypart
	 * @param recoveryAllocator
	 *            - an allocator
	 * @return new allocationMap
	 * @throws QueryPartAllocationException
	 */
	public Map<ILogicalQueryPart, PeerID> reallocateToNewPeer(Map<ILogicalQueryPart, PeerID> allocationMap,
			List<PeerID> inadequatePeers, IRecoveryAllocator recoveryAllocator) throws QueryPartAllocationException;

	/**
	 * Returns a RecoveryProcessState by identifier
	 * 
	 * @param recoveryProcessStateId
	 * @return RecoveryProcessState
	 */
	RecoveryProcessState getRecoveryProcessState(UUID recoveryProcessStateId);

	/**
	 * Allocates QueryParts of a failed peer to a new peer.
	 * 
	 * @param failedPeer
	 *            PeerID of the failed peer
	 * @param localQueryId
	 *            The local query id (from the failed peer, stored in the DDC)
	 * @param recoveryAllocator
	 *            The specific recovery allocator given by the strategy
	 * @return Map<ILogicalQueryPart, PeerID> of the new peers to allocate to
	 * @throws QueryPartAllocationException
	 */
	public Map<ILogicalQueryPart, PeerID> allocateToNewPeer(PeerID failedPeer, int localQueryId,
			IRecoveryAllocator recoveryAllocator) throws QueryPartAllocationException;
}
