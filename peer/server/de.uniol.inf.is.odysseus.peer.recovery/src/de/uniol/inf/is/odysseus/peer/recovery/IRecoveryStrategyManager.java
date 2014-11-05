package de.uniol.inf.is.odysseus.peer.recovery;

import de.uniol.inf.is.odysseus.peer.distribute.util.INamedInterface;
import net.jxta.peer.PeerID;

/**
 * Interface that decides which recovery strategy is taken
 * in which situation.
 * @author Simon Kuespert
 *
 */
public interface IRecoveryStrategyManager extends INamedInterface {
	
	/**
	 * Starts the recovery with the specific recovery strategy management.
	 *
	 * @param failedPeer Peer that failed or left the network.
	 */
	public void startRecovery(PeerID failedPeer);
}
