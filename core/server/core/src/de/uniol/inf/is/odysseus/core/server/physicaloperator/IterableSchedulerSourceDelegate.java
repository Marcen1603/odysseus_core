package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.concurrent.locks.ReentrantLock;

public class IterableSchedulerSourceDelegate implements IIterableSchedulerSource{

	private ReentrantLock lock = new ReentrantLock();
	private long delay = 0;
	private int yieldRate = 0;
	private int yieldDuration;
	
	@Override
	public boolean tryLock() {
		return lock.tryLock();
	}
	
	@Override
	public void unlock(){
		lock.unlock();
	}
	
	@Override
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	@Override
	public long getDelay() {
		return delay;
	}
	
	@Override
	public void setYieldRate(int yieldRate) {
		this.yieldRate = yieldRate;
	}
	
	@Override
	public int getYieldRate() {
		return yieldRate;
	}
	
	@Override
	public int getYieldDurationNanos() {	
		return this.yieldDuration;
	}
	
	@Override
	public void setYieldDurationNanos(int yieldDuration) {
		this.yieldDuration = yieldDuration;
	}
}
