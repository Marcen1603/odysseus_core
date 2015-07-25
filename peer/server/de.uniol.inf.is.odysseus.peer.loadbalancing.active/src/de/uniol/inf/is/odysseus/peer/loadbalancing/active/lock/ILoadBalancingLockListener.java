package de.uniol.inf.is.odysseus.peer.loadbalancing.active.lock;

public interface ILoadBalancingLockListener {
	public void notifyLockStatusChanged(boolean status);
}
