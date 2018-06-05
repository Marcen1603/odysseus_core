package de.uniol.inf.is.odysseus.core.server.physicaloperator;

public interface IIterableSchedulerSource {

	/**
	 * Set time in milliseconds to wait between two hasNext() calls
	 * Scheduler should respect this value
	 * 
	 */
	public void setDelay(long delay);
	
	/**
	 * Get time in milliseconds to wait between two hasNext() calls
	 * Scheduler should respect this value
	 * @return
	 */
	public long getDelay();	

	/**
	 * Scheduler specific value, works only in pull based schedulers
	 * @param yieldRate
	 */
	public void setYieldRate(int yieldRate);

	/**
	 * 
	 * @returns the yieldrate (-1 == do not yield)
	 */
	int getYieldRate();
	
	/**
	 * The number of nano seconds to sleep the thread for a yield
	 * @return
	 */
	public int getYieldDurationNanos();

	/**
	 * Sets the number of nano seconds that the thread should sleep each yield
	 * @param yieldDuration
	 */
	public void setYieldDurationNanos(int yieldDuration);
	
	/**
	 * Try to get a Lock 
	 * @return
	 */
	public boolean tryLock();
	
	/**
	 * Release Lock
	 */
	public void unlock();


}
