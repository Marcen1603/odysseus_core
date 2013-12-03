package de.uniol.inf.is.odysseus.core.server.physicaloperator.lock;

public class NonLockingLock extends AbstractMyLock {

	@Override
	public void lock() {
		// Do nothing
	}

	@Override
	public void unlock() {
		// Do nothing
	}

	@Override
	public boolean isLocked() {
		return false;
	}

}
