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
package de.uniol.inf.is.odysseus.priority.buffer;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class OutOfOrderBufferedPipe<T extends IStreamObject<? extends IPriority>>
		extends AbstractPrioBuffer<T> {

	public OutOfOrderBufferedPipe() {
		super();
	}
	
	public OutOfOrderBufferedPipe(
			OutOfOrderBufferedPipe<T> outOfOrderBufferedPipe) {
		super(outOfOrderBufferedPipe);
	}

	@Override
	protected synchronized void process_next(T object, int port) {
		byte prio = object.getMetadata().getPriority();

		if (prio > 0) {
			synchronized (this.buffer) {
				// TODO das adden zum puffer kapseln, so dass das incrementieren
				// auch automatisch im AbstractPrioBuffer stattfinden kann
				// es kann nicht in einer dort ueberschriebenen process_next
				// stattfinden,
				// da nicht sichergestellt ist, dass das element zum puffer
				// hinzugefuegt wird
				this.prioCount.incrementAndGet();
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
		return this.buffer.element().getMetadata().getPriority();
	}

	@Override
	public OutOfOrderBufferedPipe<T> clone() {
		return new OutOfOrderBufferedPipe<T>(this);
	}

}
