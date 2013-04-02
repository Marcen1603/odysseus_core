package de.uniol.inf.is.odysseus.core.server.physicaloperator.lock;

public interface IMyLock {

	void lock();

	void unlock();

	boolean isLocked();

}
