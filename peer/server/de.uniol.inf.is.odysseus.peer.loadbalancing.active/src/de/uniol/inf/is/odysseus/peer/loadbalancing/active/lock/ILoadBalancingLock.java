package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

public interface ILoadBalancingLock {

	public boolean requestLocalLock();
	public boolean releaseLocalLock();
}
