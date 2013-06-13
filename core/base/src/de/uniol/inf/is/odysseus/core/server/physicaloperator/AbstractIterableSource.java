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

import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public abstract class AbstractIterableSource<T> extends AbstractSource<T>
		implements IIterableSource<T> {

	private ReentrantLock lock = new ReentrantLock();
	private long delay = 0;
	
	public AbstractIterableSource() {

	}

	public AbstractIterableSource(AbstractIterableSource<T> source) {
		super(source);
	}

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
}
