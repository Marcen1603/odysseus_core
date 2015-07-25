package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

public interface ILoadBalancingLock {

	public boolean requestLocalLock();
	public boolean releaseLocalLock();
	
	public boolean isLocked();
	public void forceUnlock();
	
	public void addListener(ILoadBalancingLockListener listener);
	public void removeListener(ILoadBalancingLockListener listener);
}
