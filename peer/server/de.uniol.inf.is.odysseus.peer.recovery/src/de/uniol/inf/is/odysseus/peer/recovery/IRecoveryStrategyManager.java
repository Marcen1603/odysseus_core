package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.UUID;

import net.jxta.peer.PeerID;
import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;

/**
 * Interface that decides which recovery strategy is taken in which situation.
 * 
 * @author Simon Kuespert
 * 
 */
public interface IRecoveryStrategyManager extends INamedInterface {

	/**
	 * Starts the recovery with the specific recovery strategy management.
	 * 
	 * @param failedPeer
	 *            Peer that failed or left the network.
	 * @param recoveryStateIdentifier
	 *            - identifies the state of the recovery
	 */
	public void startRecovery(PeerID failedPeer, UUID recoveryStateIdentifier);

	/**
	 * Restarts the recovery with the specific recovery strategy management.
	 * 
	 * @param failedPeer
	 *            - the broken peer which needs recovery
	 * @param recoveryStateIdentifier
	 *            - the identifier of the RecoveryProcessState
	 * @param recoverySubStateIdentifier
	 *            - the identifier of the RecoverySubProcessState
	 */
	void restartRecovery(PeerID failedPeer, UUID recoveryStateIdentifier,
			UUID recoverySubStateIdentifier);

}
