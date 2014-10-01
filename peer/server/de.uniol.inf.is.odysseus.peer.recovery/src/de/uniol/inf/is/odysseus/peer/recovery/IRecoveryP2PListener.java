package de.uniol.inf.is.odysseus.peer.recovery;

import java.util.Observer;

/**
 * The IRecoveryPeerFailureDetector provides services to detect if a peer leaves
 * the network and to initiate the recovery
 * 
 * @author Simon Kuespert
 * 
 */
public interface IRecoveryP2PListener {
	/**
	 * Starts detection for peer failures
	 */
	public void startPeerFailureDetection();

	/**
	 * Stops detection for peer failures
	 */
	public void stopPeerFailureDetection();
	
	/**
	 * Adds a class as an observer
	 * @param obs The class which wants to be notified
	 */
	public void addObserver(Observer obs);
}
