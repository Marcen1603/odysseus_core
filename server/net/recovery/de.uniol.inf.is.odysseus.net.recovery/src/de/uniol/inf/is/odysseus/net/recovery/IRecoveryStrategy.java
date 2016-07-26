package de.uniol.inf.is.odysseus.net.recovery;

import java.util.UUID;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

/**
 * Interface for various recovery strategies that define how the recovery is
 * handled.
 * 
 * @author Simon Kuespert
 * 
 */
public interface IRecoveryStrategy extends INamedInterface {

	/**
	 * Does the recovery for the failed node (and checks, if this node can and
	 * should to this). This is the method you should call if you notice that a
	 * node failed.
	 * 
	 * @param failedNode
	 *            The PeerID from the failed peer
	 * @param inadequatePeers
	 */
	void recover(OdysseusNodeID failedNode, UUID recoveryStateIdentifier);

	/**
	 * Repeats the recovery for a single queryPart if this queryPart could not
	 * recovered in previous attempts.
	 * 
	 * @param failedNode
	 *            - the broken node which needs recovery
	 * @param recoveryStateIdentifier
	 *            - the identifier of the RecoveryProcessState
	 * @param recoverySubStateIdentifier
	 *            - the identifier of the RecoverySubProcessState
	 */
	void recoverSingleQueryPart(OdysseusNodeID failedNode,
			UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier);

	void setAllocator(IRecoveryAllocator allocator);
	
}
