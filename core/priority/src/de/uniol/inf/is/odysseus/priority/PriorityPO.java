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
package de.uniol.inf.is.odysseus.priority;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Jonas Jacobi, Jan Steinke
 */
public class PriorityPO<K extends IPriority, T extends IStreamObject<K>>
		extends AbstractPipe<T, T> {

	private final Map<Byte, IPredicate<? super T>> priorites;

	private final byte defaultPriority;

	public byte getDefaultPriority() {
		return defaultPriority;
	}

	private boolean isPunctuationActive;

	public PriorityPO(PriorityAO<T> priorityAO) {
		super();
		this.priorites = priorityAO.getPriorities();
		this.defaultPriority = priorityAO.getDefaultPriority();
		this.isPunctuationActive = priorityAO.isPunctuationActive();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T next, int port) {
		for (Map.Entry<Byte, IPredicate<? super T>> curPriority : this.priorites
				.entrySet()) {
			if (curPriority.getValue().evaluate(next)) {
				Byte priority = curPriority.getKey();
				next.getMetadata().setPriority(priority);
				transfer(next, 0);

				if (priority != 0) {
					processPrioritizedElement(next);
				}
				return;
			}
		}
		next.getMetadata().setPriority(this.defaultPriority);
		transfer(next);
		if (this.defaultPriority != 0) {
			processPrioritizedElement(next);
		}

		return;
	}

	private void processPrioritizedElement(T next) {
		transfer(next, 1);
		if (isPunctuationActive) {
			ITimeInterval time = (ITimeInterval) next.getMetadata();
			sendPunctuation(time.getStart().clone());
		}
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	public boolean isPunctuationActive() {
		return isPunctuationActive;
	}

	public void setPunctuationActive(boolean isPunctuationActive) {
		this.isPunctuationActive = isPunctuationActive;
	}

	@Override
	public PriorityPO<K, T> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof PriorityPO)) {
			return false;
		}
		@SuppressWarnings("unchecked")
		PriorityPO<K,T> ppo = (PriorityPO<K,T>) ipo;
		if (this.getSubscribedToSource().equals(ppo.getSubscribedToSource())
				&& this.getDefaultPriority() == ppo.getDefaultPriority()
				&& this.priorites.equals(ppo.priorites)) {
			return true;
		}
		return false;
	}

}
