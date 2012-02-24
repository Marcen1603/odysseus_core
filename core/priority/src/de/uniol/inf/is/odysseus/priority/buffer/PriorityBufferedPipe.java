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

import java.util.Comparator;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.core.server.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataComparator;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityBufferedPipe<T extends IMetaAttributeContainer<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	private Comparator<? super T> comparator = new MetadataComparator<IPriority>();

	public PriorityBufferedPipe() {
	}
	
	public PriorityBufferedPipe(PriorityBufferedPipe<T> priorityBufferedPipe) {
		super(priorityBufferedPipe);
	}

	@SuppressWarnings("unchecked")
	protected void process_next(T object, int port, boolean isReadOnly) {
		byte prio = object.getMetadata().getPriority();

		if (isReadOnly) {
			object = (T) object.clone();
		}

		if (prio > 0) {
			synchronized (this.buffer) {
				// TODO siehe kommentar OutOfORderBufferedPipe
				prioCount.incrementAndGet();
				ListIterator<T> li = this.buffer.listIterator(this.buffer
						.size());
				while (li.hasPrevious()) {
					if (this.comparator.compare(li.previous(), object) == -1) {
						li.next();
						li.add(object);
						return;
					}
				}
			}
			synchronized (this.buffer) {
				this.buffer.addFirst(object);
			}
		} else {
			synchronized (this.buffer) {
				this.buffer.add(object);
			}
		}
	}

	@Override
	public Byte getTopElementPrio() {
		T obj = this.buffer.peek();
		return obj.getMetadata().getPriority();
	}

	@Override
	public PriorityBufferedPipe<T> clone() {
		return new PriorityBufferedPipe<T>(this);
	}

}
