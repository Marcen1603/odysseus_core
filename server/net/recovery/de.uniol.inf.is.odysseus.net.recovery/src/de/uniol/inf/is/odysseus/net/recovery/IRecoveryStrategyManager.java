package de.uniol.inf.is.odysseus.net.recovery;

import java.util.UUID;

import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.util.INamedInterface;

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
	 * @param failedNode
	 *            Node that failed or left the network.
	 * @param recoveryStateIdentifier
	 *            - identifies the state of the recovery
	 */
	public void startRecovery(OdysseusNodeID failedNode, UUID recoveryStateIdentifier);

	/**
	 * Restarts the recovery with the specific recovery strategy management.
	 * 
	 * @param failedNode
	 *            - the broken node which needs recovery
	 * @param recoveryStateIdentifier
	 *            - the identifier of the RecoveryProcessState
	 * @param recoverySubStateIdentifier
	 *            - the identifier of the RecoverySubProcessState
	 */
	void restartRecovery(OdysseusNodeID failedNode, UUID recoveryStateIdentifier, UUID recoverySubStateIdentifier);

}
