package de.uniol.inf.is.odysseus.peer.recovery;

/**
 * The IRecoveryPeerFailureDetector provides services to detect if a peer leaves
 * the network and to initiate the recovery
 * 
 * @author Simon Kuespert
 * 
 */
public interface IRecoveryPeerFailureDetector {
	/**
	 * Starts detection for peer failures
	 */
	public void startPeerFailureDetection();

	/**
	 * Stops detection for peer failures
	 */
	public void stopPeerFailureDetection();
}
