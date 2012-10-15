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
package de.uniol.inf.is.odysseus.priority_interval;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.priority.IPriority;

//interface should be K extends IPriority & ITimeInterval, but suns compiler (1.6) is buggy and doesn't accept it
/**
 * @author Jonas Jacobi
 */
public class PriorityTITransferArea<K extends ITimeInterval, R extends IStreamObject<K>, W extends IStreamObject<K>>
		extends TITransferArea<R, W> {

	private void updateOrder(IStreamObject<K> object){
		// If prio > 0 allow out of order processing
		// do not change state if no prio!
		// cast is necessary because of a compiler bug, see above
		if (((IPriority) object.getMetadata()).getPriority() != 0) {
			object.setInOrder(false);
		}
	}
	
	@Override
	public void newElement(R object, int port) {
		updateOrder(object);
		super.newElement(object, port);
	}

	@Override
	public void transfer(W object) {
		updateOrder(object);
		if (((IPriority) object.getMetadata()).getPriority() > 0) {
			po.transfer(object);
		} else {
			super.transfer(object);
		}
	}

}
