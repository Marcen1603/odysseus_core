package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

public interface ILoadBalancingLockListener {
	public void notifyLockStatusChanged(boolean status);
}
