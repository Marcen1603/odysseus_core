package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.UUID;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import net.jxta.peer.PeerID;

/**
 * Interface for various recovery strategies that define
 * how the recovery is handled. 
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

	void recoverSingleQueryPart(PeerID failedPeer,
			UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier);
	
	void setAllocator(IRecoveryAllocator allocator);
}
