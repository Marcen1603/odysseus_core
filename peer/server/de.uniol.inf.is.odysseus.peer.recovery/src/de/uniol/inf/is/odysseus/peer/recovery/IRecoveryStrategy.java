package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.UUID;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import net.jxta.peer.PeerID;

/**
 * Interface for various recovery strategies that define how the recovery is
 * handled.
 * 
 * @author Simon Kuespert
 * 
 */
public interface IRecoveryStrategy extends INamedInterface {

	/**
	 * Does the recovery for the failed peer (and checks, if this peer can and
	 * should to this). This is the method you should call if you notice that a
	 * peer failed.
	 * 
	 * @param failedPeer
	 *            The PeerID from the failed peer
	 * @param inadequatePeers
	 */
	void recover(PeerID failedPeer, UUID recoveryStateIdentifier);

	/**
	 * Repeats the recovery for a single queryPart if this queryPart could not
	 * recovered in previous attempts.
	 * 
	 * @param failedPeer
	 *            - the broken peer which needs recovery
	 * @param recoveryStateIdentifier
	 *            - the identifier of the RecoveryProcessState
	 * @param recoverySubStateIdentifier
	 *            - the identifier of the RecoverySubProcessState
	 */
	void recoverSingleQueryPart(PeerID failedPeer,
			UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier);

	/**
	 * TODO
	 * 
	 * @param allocator
	 */
	void setAllocator(IRecoveryAllocator allocator);
}
