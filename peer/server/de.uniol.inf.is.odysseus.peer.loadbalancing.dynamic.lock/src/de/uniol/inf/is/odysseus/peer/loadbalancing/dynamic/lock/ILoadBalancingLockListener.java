package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

/**
 * Listener for Load Balancing Lock
 * @author Carsten Cordes
 *
 */
public interface ILoadBalancingLockListener {
	/**
	 * Called when Lock is locked or unlocked
	 * @param status true if Lock is locked, false if unlocked.
	 */
	public void notifyLockStatusChanged(boolean status);
}
