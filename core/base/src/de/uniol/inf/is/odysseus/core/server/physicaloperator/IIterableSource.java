/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;

/**
 * An iterator interface for plain sources or buffers.
 * While {@link #hasNext()} may block until data is available
 * for plain sources,
 * it is not allowed to block for buffers.
 * @author Jonas Jacobi
 */
public interface IIterableSource<T> extends ISource<T> {
	/**
	 * Get whether a call to transferNext() will be successful.
	 * @return true, if an element can be transfered. 
	 */
	public boolean hasNext();

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
	 * Call {@link #transfer(Object)} with the next element. May only be called
	 * if a call to hasNext() returns true.
	 */
	public void transferNext();
	
	/**
	 * Returns true, if ISource has all Input processed
	 */
	public boolean isDone();
		
	/**
	 * Indicates if the operator is currently blocked (i.e. does not produce elements)
	 * @return
	 */
	@Override
	public boolean isBlocked();

	/**
	 * Block operator
	 */
	@Override
	public void block();
	
	/**
	 * unblock operator
	 */
	@Override
	public void unblock();
	
	/**
	 * Try to get a Lock 
	 * @return
	 */
	public boolean tryLock();
	
	/**
	 * Release Lock
	 */
	public void unlock();

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
	
}

