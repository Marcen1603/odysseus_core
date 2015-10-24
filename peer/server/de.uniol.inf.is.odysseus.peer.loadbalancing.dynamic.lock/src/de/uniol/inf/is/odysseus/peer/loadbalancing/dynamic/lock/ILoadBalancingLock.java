package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

/**
 * Allows Locking of peers for redistribution of Queries during dynamic load Balancing
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingLock {

	/**
	 * Tries to acquiere Lock for Local Peer
	 * @return True if Local Peer could be locked.
	 */
	public boolean requestLocalLock();
	
	/**
	 * Releases Lock on local Peer
	 * @return true if Lock could be released.
	 */
	public boolean releaseLocalLock();
	
	/**
	 * Tests if local Peer is locked.
	 * @return true if peer is locked.
	 */
	public boolean isLocked();
	
	/**
	 * Forces unlocking of local peer (e.g. for debugging purpose or in error situations)
	 */
	public void forceUnlock();
	
	/**
	 * Returns PeerID of locking Peer.
	 * @return Peer ID of locking Peer.
	 */
	public String getLockingPeerID();
	
	/**
	 * Adds listener that is notified of locking/unlocking.
	 * @param listener
	 */
	public void addListener(ILoadBalancingLockListener listener);
	
	/**
	 * Removes listener that is notified of locking/unlocking.
	 * @param listener
	 */
	public void removeListener(ILoadBalancingLockListener listener);
	
	/**
	 * Gets new unique Locking ID (e.g. to synchronize Locking Processes)
	 * @return
	 */
	public int getNewLockingId();
}
