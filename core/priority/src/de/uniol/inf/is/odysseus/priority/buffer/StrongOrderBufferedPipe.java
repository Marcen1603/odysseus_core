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

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class StrongOrderBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	private LinkedList<T>[] buffers;

	@SuppressWarnings("unchecked")
	public StrongOrderBufferedPipe(int maxPrio) {
		super();
		buffers = new LinkedList[maxPrio + 1];
		for (int i = 0; i < maxPrio + 1; ++i) {
			buffers[i] = new LinkedList<T>();
		}
	}

	public StrongOrderBufferedPipe(
			StrongOrderBufferedPipe<T> strongOrderBufferedPipe) {
		this(strongOrderBufferedPipe.buffers.length);
	}

	@Override
	protected void process_next(T object, int port) {
		byte prio = object.getMetadata().getPriority();
		synchronized (this.buffer) {
			if (prio > 0) {
				this.prioCount.incrementAndGet();
			}
			this.buffers[prio].add(object);
			this.heartbeat.set(null);
		}
	}

	@Override
	public void transferNext() {
		transferLock.lock();
		boolean transfered = false;
		for (int i = buffers.length - 1; i > -1; --i) {
			if (!buffers[i].isEmpty()) {
				transfered = true;
				T element;
				synchronized (this.buffer) {
					element = buffers[i].pop();
				}
				transfer(element);
				if (isDone()) {
					propagateDone();
				}
				break;
			}
		}
		if (!transfered) {
			PointInTime hearbeat = this.heartbeat.get();
			if (hearbeat != null) {
				sendPunctuation(heartbeat.getAndSet(null));
			}
		}
		transferLock.unlock();
	}

	@Override
	public boolean hasNext() {
		if (!isOpen()) {
			getLogger().error("hasNext call on not opened buffer!");
			return false;
		}

		return !isBuffersEmpty() || this.heartbeat.get() != null;
	}

	@Override
	public boolean isDone() {
		transferLock.lock();
		boolean returnValue;
		synchronized (this.buffer) {
			returnValue = super.isDone() && isBuffersEmpty();
		}
		transferLock.unlock();
		return returnValue;
	}

	private boolean isBuffersEmpty() {
		for (List<T> l : this.buffers) {
			if (!l.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Byte getTopElementPrio() {
		for (int i = buffers.length - 1; i > -1; --i) {
			if (!buffers[i].isEmpty()) {
				return (byte) i;
			}
		}
		return 0;
	}

	@Override
	public StrongOrderBufferedPipe<T> clone() {
		return new StrongOrderBufferedPipe<T>(this);
	}

}
