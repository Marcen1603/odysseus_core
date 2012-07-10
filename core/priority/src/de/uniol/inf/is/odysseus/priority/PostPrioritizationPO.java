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

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

public class PostPrioritizationPO<K extends IPriority, T extends IMetaAttributeContainer<? extends K>>
		extends AbstractPipe<T, T> {

	private IPostPrioritizationArea<T> prioritizedElements;

	public PostPrioritizationPO() {
	}

	@Override
	public PostPrioritizationPO<K, T> clone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(T element, int port) {
		if (port == 0) {
			prioritizedElements.purgeElements(element);
			if (prioritizedElements.hasPartner(element)) {
				element.getMetadata().setPriority((byte) 1);
			}
			transfer(element);
		} else {
			prioritizedElements.insert(element);
		}
	}
	
	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		if (port == 0) {
			sendPunctuation(timestamp);
		}
	}

	public void setPrioritizedElementsSweepArea(IPostPrioritizationArea<T> sa) {
		this.prioritizedElements = sa;
	}
	
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof PostPrioritizationPO)) {
			return false;
		}
		PostPrioritizationPO<?,?> pppo = (PostPrioritizationPO<?,?>) ipo;
		if(this.getSubscribedToSource().equals(pppo.getSubscribedToSource())) {
			return true;
		}
		return false;
	}

}
