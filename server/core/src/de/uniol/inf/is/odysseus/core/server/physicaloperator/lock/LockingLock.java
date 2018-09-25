package de.uniol.inf.is.odysseus.core.server.physicaloperator.lock;

import java.util.concurrent.locks.ReentrantLock;

public class LockingLock extends AbstractMyLock {

	final ReentrantLock lock = new ReentrantLock();
	
	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public void unlock() {
		lock.unlock();
	}

	@Override
	public boolean isLocked() {
		return lock.isLocked();
	}

}
