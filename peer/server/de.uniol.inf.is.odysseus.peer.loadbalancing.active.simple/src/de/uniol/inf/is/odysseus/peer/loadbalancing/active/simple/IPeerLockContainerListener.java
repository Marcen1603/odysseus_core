package de.uniol.inf.is.odysseus.peer.loadbalancing.active.simple;

public interface IPeerLockContainerListener {

	public void notifyLockingFailed();
	public void notifyLockingSuccessfull();
	public void notifyReleasingFinished();
	
}
