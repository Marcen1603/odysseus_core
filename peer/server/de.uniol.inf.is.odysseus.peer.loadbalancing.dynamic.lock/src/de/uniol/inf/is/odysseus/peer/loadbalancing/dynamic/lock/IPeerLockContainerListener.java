package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

/**
 * Listener for PeerLockContainer
 * @author Carsten Cordes
 *
 */
public interface IPeerLockContainerListener {

	/**
	 * Called when Locking of at least one Peer failed.
	 */
	public void notifyLockingFailed();
	/**
	 * Called when all Peers were successfully locked.
	 */
	public void notifyLockingSuccessfull();
	/**
	 * Called when releasing locks is finished.
	 */
	public void notifyReleasingFinished();
	
}
