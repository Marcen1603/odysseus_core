package de.uniol.inf.is.odysseus.peer.loadbalancing.dynamic.lock;

public interface IPeerLockContainerListener {

	public void notifyLockingFailed();
	public void notifyLockingSuccessfull();
	public void notifyReleasingFinished();
	
}
