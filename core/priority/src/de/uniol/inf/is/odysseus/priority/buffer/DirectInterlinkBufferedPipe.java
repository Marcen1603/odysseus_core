/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.priority.buffer;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferedPipe;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class DirectInterlinkBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends BufferedPipe<T> {
	Lock directLinkLock = new ReentrantLock();

	public DirectInterlinkBufferedPipe(
			DirectInterlinkBufferedPipe<T> directInterlinkBufferedPipe) {
		super(directInterlinkBufferedPipe);
	}

	public DirectInterlinkBufferedPipe() {
	}

	@Override
	public void transferNext() {
		directLinkLock.lock();
		super.transferNext();
		directLinkLock.unlock();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(T object, int port) {
		byte prio = object.getMetadata().getPriority();

		if (prio > 0) {
			directLinkLock.lock();
			transfer(object);
			directLinkLock.unlock();
		} else {
			synchronized (this.buffer) {
				this.buffer.add(object);
				this.heartbeat.set(null);
			}
		}
	}

	@Override
	public DirectInterlinkBufferedPipe<T> clone() {
		return new DirectInterlinkBufferedPipe<T>(this);
	}

}
